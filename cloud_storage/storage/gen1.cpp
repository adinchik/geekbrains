#include <iostream>
#include <cstdio>
#include <algorithm>
#include <cmath>
#include <string>
#include <vector>
#include <stack>
#include <queue>
#include <set>
#include <cstring>
#include <map>
#include <cstdlib>
#include <ctime>
#include <cassert>
#include <bitset>
#include "testlib.h"

using namespace std;
const int N = int(2e5) + 10, mod = int(1e9)  + 7;

int a[N][10];

int main(int argc, char* argv[]) {

	registerGen(argc, argv, 1);
	int n = atoi(argv[1]), k = atoi(argv[2]);
	for(int i = 1; i <= n; i++) {
        for (int j = 1; j <= k; j++) {
            a[i][j] = rnd.next(0, 1000);
            printf("%d ", a[i][j]);
        }
        printf("\n");
	}
	return 0;
}

