(ns om-dropzone.translations
  (:use [net.unit8.tower :only [t]]))


(def translations
  {:dev-mode? false
   :fallback-locale :en
   :dictionary
   {:en {:dictFileTooBig "File is too big ({{%s}}MiB). Max filesize: {{%s}}MiB."
         :dictInvalidFileType "You can't upload files of this type."
         :dictResponseError "Server responded with {{%s}} code."
         :dictCancelUpload "Cancel upload"
         :dictCancelUploadConfirmation "Are you sure you want to cancel this upload?"
         :dictRemoveFileConfirmation ""
         :dictMaxFilesExceeded "You can not upload any more files."
         :missing! "<Translation not found>"}

    :es {:dictFileTooBig "El archivo es muy grande ({{%s}}MiB). Tamaño máximo: {{%s}}MiB."
         :dictInvalidFileType "El tipo de archivo subido no es soportado"
         :dictResponseError "El servidor respondió con el código {{%s}}."
         :dictCancelUpload "Cancelar carga de archivos"
         :dictCancelUploadConfirmation "¿Está seguro que desea cancelar la subida de archivos?"
         :dictRemoveFileConfirmation ""
         :dictMaxFilesExceeded "No se pueden subir más archivos"
         :missing! "<Traducción no encontrada>"}}})

(defn _T
  [lang k]
  (t lang translations k))