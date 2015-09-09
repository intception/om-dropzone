(ns examples.basic.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [sablono.core :as html :refer-macros [html]]
            [om-dropzone.core :refer [dropzone]]))

(enable-console-print!)

(def app-state
  (atom
    {:images {}}))

(defn my-app [cursor owner]
  (reify
    om/IDisplayName
    (display-name[_] "App")
    om/IRenderState
    (render-state [this state]
      (html [:div

             [:button#upload "Upload"]

             (om/build dropzone
                       (:images cursor)
                       {:opts {:on-success #(println "success uploading!" %)
                               :on-delete #(println "delete deleting!" %)
                               :on-added-file #(println "added file:" %)
                               :upload-url "/test/upload"
                               :create-download-link true
                               :download-file-url "/consumers/files/download/"
                               :download-file-text "Download File!"
                               :hidden false
                               :lang :es
                               :max-files 2
                               :upload-multiple false
                               :clickable "#upload"
                               :accepted-files ".png,.js,.pdf,.jpeg,.jpg"}})]))))

(om/root my-app app-state
         {:target (.getElementById js/document "app")})