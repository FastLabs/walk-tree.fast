.PHONY:hello
hello:
	echo "Hello World"
watch-scss:
	npm run scss
cljs:hello
	lein fig:build
