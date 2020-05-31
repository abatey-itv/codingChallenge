# content-technology-coding-test-template

The `resources` directory contains video files that have been downloaded from the internet archive.
This directory also contains a `checksums.csv` file that lists the expected checksums that we want
to verify these against.

The goal of this exercise is to write code that will perform this verification based on the contents
of `resources/checksums.csv`.

Code for generating an MD5 Checksum is provided in `MD5Checker.scala`.

Some points to consider:

1. How will we know our code is working as expected?
2. What should happen if the verification fails?


---

Prints 4 columns of CSV results to console in the following format: 

```
file name,PASSED/FAILED/ERROR,expected checksum,actual checksum
video1.mp4,PASSED,114b769462fa209f914dd6872883e170,114b769462fa209f914dd6872883e170
video2.mp4,FAILED,d2aa249b4bb07e646cc9a90d29a98826,d2aa249b4bb07e646cc9a90d29a98825
video4.mp4,ERROR,1526614416ec96b8a8e9817c9c680490,

```