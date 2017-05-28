#include <bits/stdc++.h>
using namespace std;

#define N 225005
int M =0;
#define inf 10000000
vector < int> base,col;
vector < vector<int> > x0,y57,x,y,g;
vector < pair<int,int> > srt;
int X,Y;
void rotate(int i){
	int n=x[i].size();
	swap(x[i][0],y[i][0]);
	for(int j=1;j<n;j++){
		swap(x[i][j],y[i][j]);
		x[i][j]=x[i][0]-x[i][j]+1;
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

void fix(int i){
	int n=x[i].size()-1;
	for(int j=1;j<=n;j++){
		g[x[i][j]][y[i][j]]=col[i];
		base[y[i][j]]=max(base[y[i][j]],x[i][j]);
		X=max(X,x[i][j]);
		Y=max(Y,y[i][j]);
	}
}

void align(int i){
	int n=x[i].size()-1,pos,ht,cmp=inf+inf,act=0,dy=0,rot=0;
	vector<int> mn;
	for(int c=0;c<4;c++){
		pull(i,inf);
		mn.clear();
		mn.resize(1+y[i][0],inf+inf);
		for(int j=1;j<=n;j++)
			mn[y[i][j]]=min(mn[y[i][j]],x[i][j]);
		for(int rt=0;y[i][0]+rt<=M;rt++){
			pos=inf+inf;ht=0;
			for(int j=1;j<=y[i][0];j++)
				pos=min(pos,mn[j]-base[j+rt]-1);

			ht=x[i][0]-pos;
			if(ht<cmp){
				act=pos;
				dy=rt;
				rot=c;
				cmp=ht;
			}
		}
		push(i,inf);
		rotate(i);
	}
	//cout<<"min ht "<<ht<<" with "<<rot<<" rots and "<<dy<<" right "<<act<<" down\n";
	while(rot--)
		rotate(i);
	move(i,inf-act,dy);
	fix(i);
	move(i,act-inf,-dy);
}


void show(){
	for(int i=1;i<=X;i++,puts(""))
		for(int j=1;j<=Y;j++)
			printf("%d ",g[i][j]);
}

int solve(){
	int n =x.size()-1;
	X=Y=0;
	g.clear();
	base.clear();
	g.resize(M*n+1);
	base.resize(M+1);
	for(int i=0;i<g.size();i++)
		g[i].resize(M+1);
	for(int i=1;i<=n;i++){
		align(i);
		// show();
	}
	return X*Y;
}

int main(){
	int n,sz;
	scanf("%d",&n);
	x0.resize(n+1);
	y57.resize(n+1);
	srt.resize(n+1);
	col.resize(n+1);
	srt[0]=make_pair(0,0);
	X=Y=0;
	for(int i=1;i<=n;i++){
		scanf("%d",&sz);
		x0[i].resize(sz+1);
		y57[i].resize(sz+1);
		for(int j=1;j<=sz;j++){
			scanf("%d%d",&x0[i][j],&y57[i][j]);
			x0[i][0]=max(x0[i][0],x0[i][j]);
			y57[i][0]=max(y57[i][0],y57[i][j]);
		}
		srt[i]=make_pair(sz,i);
		M=max(M,max(x0[i][0],y57[i][0]));
	}
	sort(srt.begin(), srt.end());
	x.resize(n+1);
	y.resize(n+1);

	for(int i=1;i<=n;i++){
		x[n+1-i]=x0[srt[i].second];
		y[n+1-i]=y57[srt[i].second];
		col[n+1-i]=i;
	}
	int m0=M,ans=inf,idx=0;
	for(M=m0;M<=2*m0;M++){
		if(solve()<ans){
			idx=M;
			ans=X*Y;
		}
	}
	M=idx;
	solve();
	cout<<X<<" "<<Y<<"\n";
	show();
}