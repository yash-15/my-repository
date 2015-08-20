#include<bits/stdc++.h>
using namespace std;

class MoveSolver
{
    const static bool debug = false;
    const static int maxSize=5;
    int gridSize,tokenCount;            // size of the square grid
    char grid[maxSize][maxSize+1];      // grid stored in row-major form
    /*      Specification for grid
        * 'X' for a box which is blocked
        * 'a'-'z' for a box which is a destination
        * all the destinations of a given color are to be marked by same character
        * '.' for other boxes
        * grid ignores the position of the balls
    */
    vector< pair<int,int> > dest;       // stores the boxes which are destinations
    char tokens[maxSize][maxSize+1];    // tokens stored in row-major form
    /*      Specification for tokens
        * 'a'-'z' for a box having token
        * '.' otherwise
        * tokens ignore the type of box in the grid
    */
    enum Move_t {Up,Down,Left,Right};   // possible moves in this game
    vector<Move_t> Move;                // list of all possible moves to iterate on
    struct stateInfo
    {
        int level;
        Move_t Move;
        string from;
    };
    map<string, stateInfo> data;

    bool isValid(char [][maxSize+1]);
    string toString(char [][maxSize+1]);
    void fromString(const string&, char [][maxSize+1]);
    bool getState(const char [][maxSize+1],char [][maxSize+1],Move_t);
    void showState(char [][maxSize+1]);
    void backTrack(string&);
public:
    void input();
    void solve();
    MoveSolver()
    {
        dest.clear();
        tokenCount=0;
        Move.clear();
        Move.push_back(Up);
        Move.push_back(Down);
        Move.push_back(Left);
        Move.push_back(Right);
    }
};

bool MoveSolver::isValid(char token[][maxSize+1])
{
    for(int i=0;i<tokenCount;i++)
            if(token[dest[i].first][dest[i].second]!=grid[dest[i].first][dest[i].second])
                return false;
    return true;
}

string MoveSolver::toString(char a[][maxSize+1])
{
    string str="";
    for(int i=0;i<gridSize;i++)
        str+=a[i];
    return str;
}

void MoveSolver::fromString(const string &str, char a[][maxSize+1])
{
    for(int i=0;i<gridSize;i++)
    {
        for(int j=0;j<gridSize;j++)
            a[i][j]=str[i*gridSize+j];
        a[i][gridSize]=0;
    }
}

bool MoveSolver::getState(const char a[][maxSize+1], char b[][maxSize+1], Move_t mv)
{
    bool change=false;
    int i,j;
    for(i=0;i<gridSize;i++)
        strcpy(b[i],a[i]);

    switch(mv)
    {
        case Up:    for(i=0;i<gridSize-1;i++)
                        for(j=0;j<gridSize;j++)
                            if(grid[i][j]=='X' || b[i][j]!='.' || b[i+1][j]=='.')
                                continue;
                            else
                            {
                                b[i][j]=b[i+1][j];
                                b[i+1][j]='.';
                                change=true;
                            }
                    break;

        case Down:  for(i=gridSize-1;i;i--)
                        for(j=0;j<gridSize;j++)
                            if(grid[i][j]=='X' || b[i][j]!='.' || b[i-1][j]=='.')
                                continue;
                            else
                            {
                                b[i][j]=b[i-1][j];
                                b[i-1][j]='.';
                                change=true;
                            }
                    break;

        case Left:     for(j=0;j<gridSize-1;j++)
                        for(i=0;i<gridSize;i++)
                           if(grid[i][j]=='X' || b[i][j]!='.' || b[i][j+1]=='.')
                                continue;
                            else
                            {
                                b[i][j]=b[i][j+1];
                                b[i][j+1]='.';
                                change=true;
                            }
                    break;

        case Right:  for(j=gridSize-1;j;j--)
                        for(i=0;i<gridSize;i++)
                            if(grid[i][j]=='X' || b[i][j]!='.' || b[i][j-1]=='.')
                                continue;
                            else
                            {
                                b[i][j]=b[i][j-1];
                                b[i][j-1]='.';
                                change=true;
                            }
                    break;
    }
    if(debug)
    {
        printf("\n");
        showState(b);
    }
    return change;
}

void MoveSolver::showState(char a[][maxSize+1])
{
    for(int i=0;i<gridSize;i++)
        printf("  %s\n",a[i]);
    puts("");
}

void MoveSolver::backTrack(string &str)
{
    stateInfo state=data[str];
    stack<Move_t> solution;
    while(state.level>1)
    {
        solution.push(state.Move);
        state=data[state.from];
    }
    char current[maxSize][maxSize+1],next[maxSize][maxSize+1];
    if(debug)
        fromString(toString(tokens),current);
    printf("  Move sequence is: ");
    while(!solution.empty())
    {
        switch(solution.top())
        {
            case Up:    printf(" Up -> ");     break;
            case Down:  printf(" Down -> ");   break;
            case Left:  printf(" Left -> ");   break;
            case Right: printf(" Right -> ");  break;
        }
        if(debug)
        {
            puts("");
            getState(current,next,solution.top());
            fromString(toString(next),current);
        }
        solution.pop();
    }
    puts(" !!!");
}

void MoveSolver::solve()
{
    if(isValid(tokens))
    {
        puts("  Already solved!");
        return;
    }

    queue< pair<string,int> > stage;    // string representation of tokens, depth
    char current[maxSize][maxSize+1],next[maxSize][maxSize+1];
    stateInfo state;
    string str = toString(tokens);
    int level;

    state.level=1;
    state.from="";
    data[str]=state;
    stage.push(make_pair(str,1));

    while(!stage.empty())
    {
        fromString(stage.front().first,current);
        level=stage.front().second;
        if(debug)
        {
            printf("Following state reached at level %d\n",level);
            showState(current);
        }
        state.level=1+level;
        state.from=stage.front().first;

        stage.pop();

        for(int m=0;m<4;m++)
            if(getState(current,next,Move[m]))
            {
                str=toString(next);
                state.Move=Move[m];

                if(isValid(next))
                {
                    printf("Solution state reached at level %d\n",level);
                    if(debug)
                        showState(next);

                    data[str]=state;
                    backTrack(str);
                    return;
                }
                else if(!data.count(str))
                {
                    if(debug)
                        printf("Above state has been pushed!\n\n");
                    data[str]=state;
                    stage.push(make_pair(str,1+level));
                }
            }
    }
    puts("  Solution does not exist!");
}



void MoveSolver::input()
{
    cout<<" Enter the size of the grid : ";
    cin>>gridSize;
    cout<<" Enter the grid : \n";
    for(int i=0;i<gridSize;i++)
    {
        cout<<" ";
        cin>>grid[i];
        grid[i][gridSize]=0;
        for(int j=0;j<gridSize;j++)
            if(grid[i][j]!='.' && grid[i][j]!='X')
                dest.push_back(make_pair(i,j));
    }
    cout<<"\n Enter the tokens : \n";
    for(int i=0;i<gridSize;i++)
    {
        cout<<" ";
        cin>>tokens[i];
        tokens[i][gridSize]=0;
    }
    tokenCount=dest.size();
}

int main()
{
    MoveSolver obj;
    obj.input();
    obj.solve();
    return 0;
}
