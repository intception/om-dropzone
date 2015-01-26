om-dropzone
=============

An [Om](https://github.com/swannodette/om) ([ClojureScript](https://github.com/clojure/clojurescript))
[Dropzone](https://github.com/enyo/dropzone) component.

[![Clojars Project](http://clojars.org/org.clojars.intception/om-dropzone/latest-version.svg)](http://clojars.org/org.clojars.intception/om-dropzone)

## TODOs

* tests

## Word of advice

This is under development, use at your own risk :)


## Usage

You need to add dropzone js/css/ to your html (local or remote)

```html
<link href="css/dropzone.css" rel="stylesheet">
<script type="text/javascript" src="js/dropzone.min.js"></script>
```

```html
<link href="https://cdnjs.cloudflare.com/ajax/libs/dropzone/3.12.0/css/dropzone.css" rel="stylesheet">
<script type="text/javascript" src="https://cdnjs.cloudflare.com/ajax/libs/dropzone/3.12.0/dropzone.min.js"></script>
```

## Example

```bash
$ lein cljsbuild once
$ open examples/basic/index.html
```


## Copyright and license

Copyright © 2015 Intception

Licensed under the EPL (see the LICENSE file).
