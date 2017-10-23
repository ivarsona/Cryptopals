#include <iostream>
#include <fstream>

using namespace std;

int main()
{
	cout << "Challenge 1: Convert hex file to base 64 string\n\n";

	ifstream inFile;
	inFile.open("input");
	if (!inFile)
	{
		cerr << "Unable to open file";
		exit(1);
	}
	cout << "File opened successfully!\n";
	


	return 0;
}

