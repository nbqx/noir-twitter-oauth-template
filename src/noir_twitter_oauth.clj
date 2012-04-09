(ns noir-twitter-oauth
  (:use [noir.core :only [defpage]]
        [noir.response :only [redirect]]
        [twitter.core]
        [twitter.oauth])
  (:require [noir.session :as session]
            [oauth.client :as oauth]))

;; twitter oauth config
(def after-connect-uri "/")
(def after-denied-uri "/bye")
(def redirect-uri "http://YOURSERVER/oauth/callback")
(def ^:dynamic *app-consumer-key* "YOUR-KEY")
(def ^:dynamic *app-consumer-secret* "YOUR-SECRET")

;; for oauth
(def ^:dynamic *consumer* (oauth/make-consumer *app-consumer-key*
                                               *app-consumer-secret*
                                               "http://twitter.com/oauth/request_token"
                                               "http://twitter.com/oauth/access_token"
                                               "http://api.twitter.com/oauth/authenticate"
                                               :hmac-sha1))

(def req-token (oauth/request-token *consumer* redirect-uri))
(def ^:dynamic *creds* (atom {}))

(defn twitter-creds []
  "return credentials"
  (do (reset! *creds*
              (make-oauth-creds *app-consumer-key*
                                *app-consumer-secret*
                                (session/get :oauth-token)
                                (session/get :oauth-token-secret)))
      (deref *creds*)))

(defn no-token? []
  "has token and secret in session or not"
  (and (nil? (session/get :oauth-token)) (nil? (session/get :oauth-token-secret))))

;; built-in url
(defpage "/oauth/connect" {}
  (let [approval-uri (oauth/user-approval-uri *consumer* (:oauth_token req-token))]
    (redirect approval-uri)))

(defpage "/oauth/callback" {:keys [oauth_token oauth_verifier denied]}
  (if (nil? denied)
    (let [access-token (oauth/access-token *consumer* req-token oauth_verifier)]
      (session/put! :oauth-token (:oauth_token access-token))
      (session/put! :oauth-token-secret (:oauth_token_secret access-token))
      (redirect after-connect-uri))
    (redirect after-denied-uri)))
  