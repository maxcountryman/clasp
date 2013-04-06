# Clasp

A dead simple routing DSL for Clojure's ring.

## Usage

Here's a simple application using Clasp:

```clojure
(ns foo.bar
  (:require [clasp.clasp :refer :all]))

(defroute foo "/" [:any] {:status 200
                          :body "hello!"
                          :headers {}})

(def app (partial wrap-routes 'foo.bar))

(run-jetty app 8080)
```

## License

Copyright © 2013 Max Countryman

Distributed under the BSD License.
