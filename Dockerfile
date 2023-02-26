from litongjava/jdk1.6


RUN mkdir -p /app
RUN sudo apt update && sudo apt install -y maven && sudo apt install -y ant



# Path: Dockerfile
COPY . app/
WORKDIR app/

