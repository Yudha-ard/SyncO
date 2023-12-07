# Use an official Node.js runtime as a smaller base image
FROM node:18-alpine AS build

# Set the working directory in the Docker container
WORKDIR /app

# Copy package.json and package-lock.json into the Docker container
COPY package*.json ./

# Install the application dependencies inside the Docker container
RUN npm install && npm cache clean --force

# Copy the rest of the application code into the Docker container
COPY . .

# Start a new stage
FROM node:18-alpine

WORKDIR /app

# Copy only the built app and the node_modules from the previous stage
COPY --from=build /app .

# Expose port 3000 for the application
EXPOSE 3000

# Define the command to run the application
CMD [ "npm", "start" ]