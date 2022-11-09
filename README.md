# walk-tree.fast

# TODO
- last update: review path definition for the clickable property.
- wouldn't it be better if there will not be entity definition, just path matchers?
- move away from material? replace it with tailwind?
- move away from figwheel?

Visualise the rest source navigation

## Overview


## Development

To compile compile/watch the stylesheet

* ``npm install`` 
* ``npm run scss``

To get an interactive development environment run:

    lein fig:build

To check the test results:
    http://localhost:9500/figwheel-extra-main/auto-testing

## TODO: 
 - move to deps (somehow got used to it already)
 - add cljc source folder (it does look that there will be shared code after all mostly for schema definition and projection definitions)
 - layout management
 - entity graph, 


To clean all compiled files:

	lein clean

To create a production build run:

	lein clean
	lein fig:min



