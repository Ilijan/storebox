; (ns storebox.file-manipulations
  ; (:import java.nio.channels.FileChannel
           ; java.nio.file.StandardOpenOption
           ; java.nio.file.Paths
           ; java.nio.ByteBuffer
           ; java.io.RandomAccessFile))

; (defn create-path [& str-paths]
  ; (apply #(Paths/get %) str-paths))

; ;; NOTE: IOException should be secured somewhere here
; (defn create-channel [path & opts]
  ; (clojure.core/cast FileChannel (apply (fn [& all]
                                          ; (let [rand-access-file (apply java.io.RandomAccessFile. all)]
                                            ; (.getChannel rand-access-file)))
                                        ; path opts)))

; (defn allocate-bytebuffer [size]
  ; (clojure.core/cast ByteBuffer (apply #(ByteBuffer/allocate %) size)))

; (defn to-bytebuffer [bytes]
  ; (if-let [xs (seq bytes)]
    ; (ByteBuffer/wrap (byte-array (map byte bytes)))))

; (defn read-from-channel [channel bytebuffer]
  ; (while (and (not= (.read channel bytebuffer) -1)
              ; (.hasRemaining bytebuffer))
    ; nil))

; (defn write-to-channel [channel bytebuffer]
  ; (while (.hasRemaining bytebuffer)
    ; (.write channel bytebuffer)))

; (defn lock-file-part [channel offset length shared]
  ; (.lock channel offset length shared))

; (defn lock-file [channel]
  ; (.lock channel))

; ;; TODO: try lock when reading

; ; (defn write-channel-part [channel offset bytes])

; ;; REVIEW: locking
; (defn write-file [path offset bytes]
  ; (let [java-path (create-path path)
        ; bytebuffer (to-bytebuffer bytes)]
    ; (with-open [fc (create-channel java-path "w")]
      ; (with-open [lock (lock-file-part fc offset (count bytes) true)]
        ; (.write fc bytebuffer offset)))))

; ;; REVIEW: https://github.com/Raynes/fs/blob/master/src/me/raynes/fs.clj