(ns clasp.util
  (:require [clojure.string :refer [upper-case]]
            [ring.util.response :refer [content-type response]]))


(defn html-response
  [body]
  (->
    (response body)
    (content-type "text/html")))


(def render-not-found
  (str "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">
       <title>404 Not Found</title>
       <h1>Not Found</h1>
       <p>The requested URL was not found on the server.</p>
       <p>If you entered the URL manually please check your spelling and try "
       "again.</p>"))


(def not-found
  (html-response render-not-found))


(defn- render-method-not-allowed
  [meth]
  (str "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">
        <title>405 Method Not Allowed</title>
        <h1>Method Not Allowed</h1>
        <p>The method " meth " is not allowed for the requested URL.</p>"))


(defn method-not-allowed
  [meth allowed]
  {:status 405
   :headers {"Allow" (upper-case (name allowed))}
   :body (render-method-not-allowed (upper-case (name meth)))})
