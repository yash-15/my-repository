#include <bits/stdc++.h>
using namespace std;

#define N 225005
#define M 155
#define inf 10000000
int g[N][M];
vector < int> base;
vector < vector<int> > x,y;
int X,Y;
void rotate(int i){
	int n=x[i].size();
	swap(x[i][0],y[i][0]);
	for(int j=1;j<n;j++){
		swap(x[i][j],y[i][j]);
		x[i][j]=x[i][0]-x[i][j];
	}
}

void move(int i, int dx, int dy){
	int n=x[i].size();
	for(int j=0;j<n;j++){
		x[i][j]+=dx;
		y[i][j]+=dy;
	}
}

void pull(int i, int val){
	move(i,val,0);
}

void push(int i, int val){
	move(i,-val,0);
}

void align(int i){
	int n=x[i].size()-1,pos,act=0,dy=0;

	vector<int> mn(y[i][0]+1,inf+inf);
	for(int j=1;j<=n;j++)
		mn[y[i][j]]=min(mn[y[i][j]],x[i][j]);
	for(int rt=0;y[i][0]+rt<M;rt++){
		pos=inf;
		for(int j=1;j<=y[i][0];j++)
			pos=min(pos,mn[j]-base[j]-1);
		if(pos>act){
			act=pos;
			dy=rt;
		}
	}
	move(i,-act,dy);
}

void fix(int i){
	int n=x[i].size()-1;
	for(int j=1;j<=n;j++){
		g[x[i][j]][y[i][j]]=i;
		base[y[i][j]]=max(base[y[i][j]],x[i][j]);
		X=max(X,x[i][j]);
		Y=max(Y,y[i][j]);
	}
}

int main(){
	int n,sz;
	base.resize(M);
	scanf("%d",&n);
	x.resize(n+1);
	y.resize(n+1);
	X=Y=0;
	for(int i=1;i<=n;i++){
		scanf("%d",&sz);
		x[i].resize(sz+1);
		y[i].resize(sz+1);
		for(int j=1;j<=sz;j++){
			scanf("%d%d",&x[i][j],&y[i][j]);
			x[i][0]=max(x[i][0],x[i][j]);
			y[i][0]=max(y[i][0],y[i][j]);
		}

		if(x[i][0]>y[i][0])
			rotate(i);
		pull(i,inf);
		align(i);
		fix(i);
	}
	cout<<X<<" "<<Y<<"\n";
	for(int i=1;i<=X;i++,puts(""))
		for(int j=1;j<=Y;j++)
			printf("%d ",g[i][j]);
}