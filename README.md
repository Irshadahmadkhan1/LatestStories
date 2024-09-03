# Time.com Latest Stories Fetcher

This project is a simple Java-based web server application that fetches the latest 6 stories from the Time.com website and returns them as a JSON array. The application is built using the built-in Java HTTP server and utilizes regular expressions for parsing HTML content.

![Screenshot 2024-09-03 202034](https://github.com/user-attachments/assets/b7a77edf-3859-4ae6-a25b-5d28057604bb)

## Project Structure

- **Main.java**: The main class that sets up the HTTP server and handles requests to fetch the latest stories from Time.com.

## Features

- **Fetch Latest Stories**: The application fetches the latest 6 stories from the Time.com homepage.
- **JSON Response**: The extracted stories are returned in a JSON format, with each object containing the story's title and link.

## Requirements

- **Java 8** or higher
- **Internet Connection**: Required to fetch data from Time.com

## How to Run

1. **Clone the repository**:
   ```bash
   git clone https://github.com/your-username/time-stories-fetcher.git
   cd time-stories-fetcher
