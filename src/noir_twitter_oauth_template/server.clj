(ns noir-twitter-oauth-template.server
  (:use [noir.core :only [defpage pre-route]]
        [noir.response :only [redirect]]
        [twitter.api.restful])
  (:require [noir.server :as server]
            [noir-twitter-oauth :as tw]))

(server/load-views "src/noir_twitter_oauth_template/views/")

(defn -main [& m]
  (let [mode (keyword (or (first m) :dev))
        port (Integer. (get (System/getenv) "PORT" "8080"))]
    (server/start port {:mode mode
                        :ns 'noir-twitter-oauth-template})))

;; example
(pre-route "/" {} (if (tw/no-token?) (redirect "/oauth/connect")))

(defpage "/" {}
  (apply str (map #(:text %) (:body (user-timeline :oauth-creds (tw/twitter-creds))))))

(defpage "/bye" {} "denied!!")

