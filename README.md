# clojurewalk

## Start

```bash
git clone git@github.com:your-user-name/clojurewalk.git
cd clojurewalk
make db/create
make db/migrate
```

## Dev

```bash
make repl # starts an nrepl server
```

```clojure
; in your editor, connect to the nrepl server
; in atom with proto-repl, it's Ctrl+Cmd+Y then enter
; then when you're in the repl evaluate -main
(-main)
```

```bash
curl http://localhost:1337 # or just open it in your browser
```

## Ship
```bash
make db/migrate
make assets
make uberjar
java -jar target/clojurewalk.jar 1337
```

## Design

Colors:

* White #ececec
* Green #62b146
* Black #2c2c2c
