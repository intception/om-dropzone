(ns examples.basic.core
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [om-dropzone.core :refer [dropzone]]))

(enable-console-print!)

(def app-state
  (atom
    {:images []}))

(defn my-app [cursor owner]
  (reify
    om/IDisplayName
       (display-name[_] "App")
    om/IRenderState
    (render-state [this state]
                  (dropzone cursor :images {:on-success #(println "success")
                                            :on-delete  #(println "delete")}))))

(om/root my-app app-state
         {:target (.getElementById js/document "app")})