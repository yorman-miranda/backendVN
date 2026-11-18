FROM ubuntu:latest
LABEL authors="YAML"

ENTRYPOINT ["top", "-b"]