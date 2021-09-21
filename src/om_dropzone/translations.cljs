(ns om-dropzone.translations
  (:use [net.unit8.tower :only [t]]))


(def translations
  {:dev-mode? false
   :fallback-locale :en
   :dictionary
   {:en {:dictFileTooBig "File is too big ({{filesize}}MiB). Max filesize: {{maxFilesize}}MiB."
         :dictInvalidFileType "You can't upload files of this type."
         :dictResponseError "Server responded with {{statusCode}} code."
         :dictCancelUpload "Cancel upload"
         :dictCancelUploadConfirmation "Are you sure you want to cancel this upload?"
         :dictRemoveFileConfirmation "Are you sure you want to delete this file?"
         :dictMaxFilesExceeded "You can not upload any more files."
         :dictDefaultMessage "Drop files here to upload"
         :dictRemoveFile "Remove file"
         :missing! "<Translation not found>"}

    :es {:dictFileTooBig "El archivo es muy grande ({{filesize}}MiB). Tamaño máximo: {{maxFilesize}}MiB."
         :dictInvalidFileType "El tipo de archivo subido no es soportado"
         :dictResponseError "El servidor respondió con el código {{statusCode}}."
         :dictCancelUpload "Cancelar carga de archivos"
         :dictCancelUploadConfirmation "¿Está seguro que desea cancelar la subida de archivos?"
         :dictRemoveFileConfirmation "¿Está seguro que desea borrar el archivo?"
         :dictDefaultMessage "Arrastre aquí sus archivos para poder subirlos."
         :dictMaxFilesExceeded "No se pueden subir más archivos"
         :dictRemoveFile "Borrar archivo"
         :missing! "<Traducción no encontrada>"}}})

(defn _T
  [lang k]
  (t lang translations k))