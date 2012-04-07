(ns noir-twitter-oauth-template.views.welcome
  (:require [noir-twitter-oauth-template.views.common :as common]
            [noir.content.getting-started])
  (:use [noir.core :only [defpage]]
        [hiccup.core :only [html]]))

(defpage "/welcome" []
         (common/layout
           [:p "Welcome to noir-twitter-oauth-template"]))
