# tick

A Clojure library for dealing with time.

## Install

Add to your lein or boot dependencies:

[![Clojars Project](http://clojars.org/tick/latest-version.svg)](http://clojars.org/tick)

[![CircleCIStatus](https://circleci.com/gh/juxt/tick.svg?style=shield&circle-token=43344d03de5e6ee2fab96d3b3e1046e7bb8fd7ab)](https://circleci.com/gh/juxt/tick)

## Timelines

Tick adds the concept of immutable *timelines* to java.time. A
timeline is a lazy but orderered sequence of `java.time.ZonedDateTime`
instances.

Timelines are easy to generate. One generator is `periodic-seq`, which
generates a uniform series of times separated by a fixed period.

```clojure
(require '[tick.core :refer [minutes]]
          [tick.timeline :refer [timeline periodic-seq]]
          [tick.clock :refer [now]])

;; A timeline of 15 minute intervals
(take 10
  (timeline (periodic-seq (now) (minutes 15))))
```

Timelines are sequences of ticks, and ticks are Clojure maps. Here's one:

```clojure
{:tick/date #object[java.time.ZonedDateTime 0x5a56528d "2012-12-04T05:21Z[Europe/London]"]}
```

Timelines can be finite or, or of course, infinite so

```clojure
(set! *print-length* 16)
```

to avoid eternal waits.

## Interleaving timelines

Multiple timelines can be interleaved into a consolidated timeline.

`interleave-timelines` takes any number of timelines and returns a
conslidated time-ordered timeline.  amalgamation.

```clojure
(require '[tick.cal :refer [easter-fridays]])

(def holidays
  (interleave-timelines
    (timeline (bank-holiday-mondays))
    (timeline (easter-fridays))))
```

## Schedules

Clojure's `map` takes a function and applies it to every item in a
sequence. Similarly, tick's `schedule` takes a function and applies it
to timelines.

Schedules can be run by starting them with a clock.

```clojure
(def schedule (t/schedule println timeline))

(t/start schedule (t/clock-ticking-in-seconds))
```

You can `stop`, `pause` and `resume` schedules too.

## Testing

A type of schdule, useful for testing, can be created with `simulate`.

```clojure
(def simulator (simulate println timeline))

(start simulator (fixed-clock ...))
```

If you want to wait for a ticker to complete its journey over a
timeline, `deref` the result of `start`.

At any time after starting a ticker you can find out its future
timeline with `remaining`, while inspecting its clock with `clock`.

## java.time

Most Clojure time libraries use clj-time, but tick uses
[**java.time**](http://www.oracle.com/technetwork/articles/java/jf14-date-time-2125367.html)
which means that it requires Java 8.

Java 8's `java.time` API is both influenced by, and superior to, Joda
Time. It is a modern immutable API that supports functional
programming. Unless you're stuck on Java 7 or ambivalent about library
choice, you should be using `java.time`.

## Discussion

The 'flow of time' complicates our systems and we can simplify them if
we separate the concept of a timeline from the 'flow' of time we
perceive.

For more information, see the author's talk at ClojuTRE-2016 on [The
Universe As A Value](https://www.youtube.com/watch?v=odPAkEO2uPQ)
where he argues that time is a perception.

## Acknowledgements

Tick is based on the same original idea as
[Chime](https://github.com/jarohen/chime). The motivation is to be
able to view timelines of remaining times while the schedule is
running. Thanks to James Henderson for his work on Chime.

## Comparison to Quartz

See https://dzone.com/articles/why-you-shouldnt-use-quartz

## Copyright & License

The MIT License (MIT)

Copyright © 2016 JUXT LTD.

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
