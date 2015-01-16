(ns om-dropzone.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [intception-widgets.core :as w]
            [cljs.reader :as reader]
            [om-dropzone.translations :refer [_T]]))


(defn dropzone-component [cursor owner]
  (reify
    om/IDisplayName
    (display-name[_] "Dropzone")

    om/IInitState
    (init-state [this]
                {:eids #{}})
    om/IWillMount
    (will-mount [this]
                (set! (.-autoDiscover js/Dropzone) false))

    om/IDidMount
    (did-mount [this]
               (let [eids (om/get-state owner :eids)
                     cursor-path (om/get-state owner :cursor-path)
                     upload-url (om/get-state owner :upload-url)
                     thumbnail-url (om/get-state owner :thumbnail-url)
                     lang (om/get-state owner :lang)
                     on-delete (om/get-state owner :on-delete)
                     on-success (om/get-state owner :on-success)
                     dropzone (js/Dropzone.
                                (om/get-node owner)
                                (clj->js {:addRemoveLinks true
                                          :parallelUploads 2
                                          :thumbnailHeight nil
                                          :thumbnailWidth nil
                                          :url upload-url
                                          :dictRemoveFile (_T lang ::dictRemoveFile)
                                          :dictDefaultMessage (_T lang ::dictDefaultMessage)
                                          :dictFallbackMessage (_T lang ::dictFallbackMessage)
                                          :dictFallbackText (_T lang ::dictFallbackText)
                                          :dictFileTooBig (_T lang ::dictFileTooBig)
                                          :dictInvalidFileType (_T lang ::dictInvalidFileType)
                                          :dictResponseError (_T lang ::dictResponseError)
                                          :dictCancelUpload (_T lang ::dictCancelUpload)
                                          :dictCancelUploadConfirmation (_T lang ::dictCancelUploadConfirmation)
                                          :dictRemoveFileConfirmation (_T lang ::dictRemoveFileConfirmation)
                                          :dictMaxFilesExceeded (_T lang ::dictMaxFilesExceeded)
                                          :init (fn []
                                                  (this-as dropzone
                                                           (.on dropzone "removedfile"
                                                                (fn [f]
                                                                  (let [id (or (get (js->clj f) "id") (.-id f))]
                                                                    (om/update-state! owner
                                                                                      :eids (fn [eids]
                                                                                              (into #{} (remove #(= id %) eids))))
                                                                    (w/om-update! cursor cursor-path (fn [file-list]
                                                                                                       (filter (fn [e] (not= (:id e) id )) file-list)))
                                                                    (when on-delete (on-delete id)))))

                                                           (.on dropzone "success"
                                                                (fn [f response]
                                                                  (let [file (reader/read-string response)]
                                                                    (when on-success (on-success (:id response)))
                                                                    (set! (.-id f) (:id file))
                                                                    (om/update-state! owner :eids (fn [eids]
                                                                                                    (conj eids (:id file))))
                                                                    (w/om-update! cursor cursor-path (fn [file-list] (conj file-list file)))
                                                                    (when on-success (on-success (:id response))))))))}))]
                 (om/update-state! owner (fn [_]
                                           {:eids (reduce (fn [s file]
                                                            (if-not (contains? s (:id file))
                                                              (let [mock-file (clj->js {:id (:id file)
                                                                                        :name (:filename file)
                                                                                        :size (:size file)})]
                                                                (.options.addedfile.call dropzone dropzone mock-file)
                                                                (.options.thumbnail.call dropzone dropzone mock-file (str thumbnail-url (:id file)))
                                                                (conj s (:id file)))
                                                              s))
                                                          eids
                                                          (w/om-get cursor cursor-path))
                                            :dropzone dropzone}))))
    om/IRenderState
    (render-state [this state]
                  (dom/div #js {:className "uploader dropzone"}))))


;; ---------------------------------------------------------------------
;; Public

(defn dropzone [cursor cursor-path {:keys [upload-url thumbnail-url on-delete on-success lang]
                                    :or {upload-url "/api/image/upload"
                                         thumbnail-url "/api/image/thumbnail/"
                                         lang :en}}]
  (om/build dropzone-component cursor {:init-state {:upload-url upload-url
                                                    :cursor-path cursor-path
                                                    :on-delete on-delete
                                                    :on-success on-success
                                                    :lang lang
                                                    :thumbnail-url thumbnail-url}}))