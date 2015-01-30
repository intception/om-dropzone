(ns om-dropzone.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [cljs.reader :as reader]
            [schema.core :as s]
            [om-dropzone.translations :refer [_T]]))


(defn- build-dropzone-instance [cursor owner opts]
  (js/Dropzone.
    (om/get-node owner)
    (clj->js (-> {:addRemoveLinks true
                  :thumbnailHeight nil
                  :thumbnailWidth nil
                  :dictRemoveFile (_T (:lang opts) :dictRemoveFile)
                  :dictDefaultMessage (_T (:lang opts) :dictDefaultMessage)
                  :dictFallbackMessage (_T (:lang opts) :dictFallbackMessage)
                  :dictFallbackText (_T (:lang opts) :dictFallbackText)
                  :dictFileTooBig (_T (:lang opts) :dictFileTooBig)
                  :dictInvalidFileType (_T (:lang opts) :dictInvalidFileType)
                  :dictResponseError (_T (:lang opts) :dictResponseError)
                  :dictCancelUpload (_T (:lang opts) :dictCancelUpload)
                  :dictCancelUploadConfirmation (_T (:lang opts) :dictCancelUploadConfirmation)
                  :dictRemoveFileConfirmation (_T (:lang opts) :dictRemoveFileConfirmation)
                  :dictMaxFilesExceeded (_T (:lang opts) :dictMaxFilesExceeded)
                  :init (fn []
                          (this-as dropzone
                                   (.on dropzone "removedfile"
                                        (fn [f]
                                          (let [id (or (get (js->clj f) "id") (.-id f))]
                                            (om/update-state! owner
                                                              :eids (fn [eids]
                                                                      (into #{} (remove #(= id %) eids))))
                                            (om/transact! cursor #(filter (fn [e]
                                                                           (not= (:id e) id))
                                                                         %))
                                            (when (:on-delete opts)
                                              ((:on-delete opts) id)))))

                                   (.on dropzone "success"
                                        (fn [f response]
                                          (let [file (reader/read-string response)]
                                            ;; update dom node id
                                            (set! (.-id f) (:id file))
                                            (om/update-state! owner :eids (fn [eids]
                                                                            (conj eids (:id file))))
                                            (om/transact! cursor #(conj % file))
                                            (when (:on-success opts)
                                              ((:on-success opts) (:id response))))))))}

                 (merge (when (:accepted-files opts)
                          {:acceptedFiles (:accepted-files opts)}))

                 (merge (when (:max-files opts)
                          {:maxFiles (:max-files opts)}))

                 (merge (when (:upload-multiple opts)
                          {:uploadMultiple (:upload-multiple opts)}))

                 (merge (when (:parallel-uploads opts)
                          {:parallelUploads (:parallel-uploads opts)}))

                 (merge (when (:upload-url opts)
                          {:url (:upload-url opts)}))

                 (merge (when (:clickable opts)
                          {:clickable (:clickable opts)}))))))

;; ---------------------------------------------------------------------
;; Schema

(def Options
  {:upload-url s/Str
   :thumbnail-url s/Str
   (s/optional-key :on-success) (s/pred fn?)
   (s/optional-key :on-delete) (s/pred fn?)
   (s/optional-key :lang) (s/enum :en :es)
   (s/optional-key :max-files) s/Int
   (s/optional-key :parallel-uploads) s/Int
   (s/optional-key :upload-multiple) s/Bool
   (s/optional-key :clickable) s/Str           ;; CSS selector to make a button an uploader
   (s/optional-key :accepted-files) s/Str})    ;; Allowed extensions, something like: ".png,.js"

;; ---------------------------------------------------------------------
;; Public

(defn dropzone [cursor owner opts]
  (reify
    om/IDisplayName
    (display-name[_] "Dropzone")

    om/IInitState
    (init-state [this]
                {:eids #{}})
    om/IWillMount
    (will-mount [this]
                (set! (.-autoDiscover js/Dropzone) false))

    om/IWillUnmount
      (will-unmount [_]
        (doto (om/get-state owner :dropzone)
          (.off "removedfile")
          (.destroy)))

    om/IDidMount
    (did-mount [this]
               (let [eids (om/get-state owner :eids)
                     dropzone (build-dropzone-instance cursor owner opts)]
                 (om/update-state! owner (fn [_]
                                           {:eids (reduce (fn [s file]
                                                            (if-not (contains? s (:id file))
                                                              (let [mock-file (clj->js {:id (:id file)
                                                                                        :name (:filename file)
                                                                                        :size (:size file)})]
                                                                (.options.addedfile.call dropzone
                                                                                         dropzone
                                                                                         mock-file)
                                                                (.options.thumbnail.call dropzone
                                                                                         dropzone
                                                                                         mock-file
                                                                                         (str (:thumbnail-url opts)
                                                                                              (:id file)))
                                                                (conj s (:id file)))
                                                              s))
                                                          eids
                                                          cursor)
                                            :dropzone dropzone}))))
    om/IRenderState
    (render-state [this state]
                  (dom/div #js {:className "uploader dropzone"}))))
