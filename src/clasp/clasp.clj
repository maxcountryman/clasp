;; ## A simple routing DSL
;;
;; Clasp is a simple routing DSL for Clojure's ring inspired by Flask.
;;
;; This library intends to provide routing for cases where you want to return
;; a 405 Method Not Allowed when a URI matches but the HTTP method does not and
;; bind to many or all HTTP methods.
;;
;; To acheive this, Clasp's API is modeled after Python's Flask. Routing in
;; Flask is presented in such a way that a URI and a set of allowed HTTP
;; methods is bound to a view function. Clasp allows you to do something
;; similar by indicating the URI and allowed methods.

(ns clasp.clasp
  (:require [clasp.util :refer [html-response method-not-allowed not-found]]
            [clout.core :refer [route-matches]]
            [clojure.string :refer [join upper-case]]
            [ring.middleware.head :refer [wrap-head]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.util.response :refer [content-type response]]))

(declare get-allowed)


;; ## Route Registry


(defmacro defroute
  "
  Give a `routename` string, a `uri` string, a `meths` vector, and a `handler`
  function, defines a function bound to `routename` in the current namespace.

  For example, we might create a route for our Ring application like this:

    (defroute foo \"/foo/:bar\" [:get]
      (fn [req] (->
                  req
                  :route-params
                  :bar
                  response)))

  Returns nil.
  "
  [routename uri meths handler]
  `(defn
     ~(with-meta routename (assoc (meta routename) :route-handler true))
    [req#]
    (let [req-meth# (:request-method req#)
          bad-meth# (nil? (some #(= req-meth# %) ~meths))
          any-meth# (= ~meths [:any])]
      (if (and (route-matches ~uri req#) (and bad-meth# (not any-meth#)))
        (method-not-allowed req-meth# (get-allowed ~meths))
        (if-let [params# (route-matches ~uri req#)]
          (~handler (assoc req# :route-params params#))
          req#)))))


(defn wrap-routes [from-ns req]
  "
  Given a `from-ns` and a `req` map, finds all functions in `from-ns`
  containing the `{:route-handler true}` metadata and passes the `req` map to
  each found function. Finally returning the first response containing the
  `:status` key or `not-found`.

  This can be invoked like so:

    (def app (partial wrap-routes 'my-ns.foo) ...)

  Where `'my-ns.foo` is the namespace you would like to pull registered route
  handlers from.
  "
  (or (first (filter :status
                     (for [[_ f] (ns-publics from-ns)
                           :when (:route-handler (meta f))]
                       (f req))))
      not-found))


(defn get-allowed
  [meths]
  (join " " (map #(upper-case (name %)) meths)))
