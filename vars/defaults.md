# defaults

:exclamation: This step is marked as deprecated

The `defaults` provide getters for defaults like
* Pipeline triggers

## Table of contents
* [getTriggers](#gettriggers)
    * [Usage Example](#usage-example)

## getTriggers

This function returns the default triggers which should be used by all
ci jobs.

### Usage Example

``` groovy
Map triggerConfig = [
    (PROPERTIES): [
        (PROPERTIES_PIPELINE_TRIGGERS) : defaults.getTriggers()
    ]
]
```
