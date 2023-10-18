
# Limited Memory Dictionary Sorter

The Limited Memory Dictionary Sorter is a Java application that allows you to read a file, sort its contents, and write the sorted data to another file.

## Getting Started

To use the Limited Memory Dictionary Sorter, follow these steps:

1. Clone the repository to your local machine.
2. Open the project in your favorite Java IDE.
3. Make sure you have Java installed on your machine.
4. Build the project to compile the source code.

## Usage

The Limited Memory Dictionary Sorter provides a simple interface. You need to provide the dictionary as input file with the following structure:
"term" + ":" + "definition" is placed on each new line.

## Configuration

The Limited Memory Dictionary Sorter uses the following configuration options, which can be modified in the `com.mablab.constants.Constants.java` file:

- `INPUT_FILE`: The path to the input file.
- `OUTPUT_FILE`: The path to the output file.
- `TEMP_FILE_PREFIX`: The prefix used for temporary files during sorting.
- `BUFFER_SIZE`: The buffer size used for reading a part from input file.

## Contributing

If you would like to contribute to the Limited Memory Dictionary Sorter, please follow these guidelines:

1. Fork the repository.
2. Create a new branch for your feature or bug fix.
3. Make your changes and commit them with descriptive commit messages.
4. Push your changes to your forked repository.
5. Submit a pull request to the main repository.