FROM mcr.microsoft.com/vscode/devcontainers/java:11

# Install Android SDK
ENV ANDROID_HOME /opt/android-sdk-linux
ENV PATH ${PATH}:${ANDROID_HOME}/tools:${ANDROID_HOME}/platform-tools

RUN apt-get update && apt-get install -y wget unzip \
    && wget https://dl.google.com/android/repository/commandlinetools-linux-7302050_latest.zip \
    && mkdir -p ${ANDROID_HOME}/cmdline-tools \
    && unzip commandlinetools-linux-7302050_latest.zip -d ${ANDROID_HOME}/cmdline-tools \
    && mv ${ANDROID_HOME}/cmdline-tools/cmdline-tools ${ANDROID_HOME}/cmdline-tools/latest \
    && rm commandlinetools-linux-7302050_latest.zip

# Install SDKMAN
RUN curl -s "https://get.sdkman.io" | bash

# Install Gradle
RUN bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install gradle"

# Clean up
RUN apt-get autoremove -y \
    && apt-get clean -y \
    && rm -rf /var/lib/apt/lists/*