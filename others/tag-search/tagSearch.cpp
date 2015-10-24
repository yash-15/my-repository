#include<bits/stdc++.h>
using namespace std;

string tag,defDir,dir,tmpFile,openFile,cmd;
char txt[100];

vector<int> pos;

// search the tag and save result in tmpFile
void searchTag(string tag, string dir)
{
	// create temporary file
	srand(time(NULL));
	int num=rand()%10000;
	sprintf(txt,"tmp00%d.ykb",num);
	tmpFile = txt;

	// system-command to search tag
	cmd="findstr /l /s /i /m /d:"+dir+" \"#"+tag+"\" *.cpp > "+tmpFile;
	system(cmd.c_str());

	//cerr<<"Command executed successfully!\n"<<tag<<" "<<dir<<" "<<tmpFile<<"\n";
}

int main()
{
	cout<<"ENTER THE TAG TO SEARCH: ";
	cin>>tag;
	
	defDir="D:/";
	cout<<"\nCURRENT DIRECTORY IS: "<<defDir<<"\nENTER NEW DIRECTORY (PRESS 0 TO SKIP): ";
	cin>>dir;
	if(dir=="0")
		dir=defDir;

	int counter=0,choice;
	searchTag(tag,dir);

	FILE *buf=fopen(tmpFile.c_str(),"r");

	pos.push_back(0);
	fscanf(buf," %*s");
	pos.push_back(ftell(buf));
	
	cout<<"\nSEARCH RESULTS\n\n";

	while(~ fscanf(buf," %s",txt))
	{
		counter++;
		pos.push_back(ftell(buf));
		printf("%3d.  %s\n",counter,txt);
	}
	while(1)
	{
		cout<<"\nENTER THE FILE NUMBER THAT YOU WANT TO OPEN (0 TO EXIT): ";
		cin>>choice;
		if(!choice)
			break;
		else if(choice<0 || choice>counter)
			cout<<"Invalid choice!";
		else
		{
			fseek(buf,pos[choice],SEEK_SET);
			fscanf(buf," %s",txt);
			openFile=txt;
			cmd="sublime_text "+dir+openFile;
			system(cmd.c_str());
		}
	}

	fclose(buf);
	cmd="del "+tmpFile;
	system(cmd.c_str());
	
	return 0;
}
