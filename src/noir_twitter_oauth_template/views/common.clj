(ns noir-twitter-oauth-template.views.common
  (:use [noir.core :only [defpartial]]
        [hiccup.page-helpers :only [include-css html5]]))

(defpartial layout [& content]
            (html5
              [:head
               [:title "noir-twitter-oauth-template"]
               (include-css "/css/reset.css")]
              [:body
               [:div#wrapper
                content]]))
