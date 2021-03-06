;; Copyright © 2016-2017, JUXT LTD.

(ns tick.schedule-test
  (:require
   [clojure.test :refer :all]
   [tick.timeline :refer [periodic-seq timeline]]
   [tick.core :refer [seconds millis]]
   [tick.clock :refer [clock-ticking-in-seconds just-now]]
   [tick.schedule :as sched]))

(deftest schedule-test
  (let [a (atom 0)
        f (fn [dt] (swap! a inc))
        clk (clock-ticking-in-seconds)
        now (just-now clk)
        timeline (take 10 (timeline (periodic-seq now (millis 10))))]
    @(sched/start (sched/schedule f timeline) clk)
    (is (= @a 10))))

(deftest simulate-test
  (let [a (atom 0)
        f (fn [dt] (swap! a inc))
        clk (clock-ticking-in-seconds)
        now (just-now clk)
        timeline (take 1000 (timeline (periodic-seq now (seconds 1))))]
    @(sched/start (sched/simulate f timeline) clk)
    (is (= @a 1000))))
