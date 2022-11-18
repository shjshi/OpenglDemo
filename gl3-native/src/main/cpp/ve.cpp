#include <string.h>
#include <vector>
#include <iostream>
using namespace std;

int main()
{
    vector<int> myVec;
    myVec.reserve( 100 );     // 新元素还没有构造,
    // 此时不能用[]访问元素
    for (int i = 0; i < 100; i++ )
    {
        myVec.push_back( i ); //新元素这时才构造
    }
    for(int i=0;i<myVec.size();i++)//size()容器中实际数据个数
    {
        cout<<myVec[i]<<",";
    }
    myVec.resize( 102 );      // 用元素的默认构造函数构造了两个新的元素
    myVec[100] = 1;           //直接操作新元素
    myVec[101] = 2;
    cout<<myVec[100]<<",\n";
    cout<<myVec[101]<<",\n";
    return 0;
}

