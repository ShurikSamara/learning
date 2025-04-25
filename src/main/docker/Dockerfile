FROM eclipse-temurin:21-jdk

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/*

# Set working directory
WORKDIR /app

# Copy the project files
COPY ../../.. .

# Build the project to download dependencies
RUN mvn dependency:go-offline

# Default environment variable for application properties
ENV ENV=application

# Command to run tests (can be overridden)
ENTRYPOINT ["mvn", "clean", "test"]