#!/bin/bash
setxkbmap pt
cd /media/sf_partilha/sprint2/utests/usac08
export REPO=user_stories
make -B
make run