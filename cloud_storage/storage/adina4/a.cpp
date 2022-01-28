#include <bits/stdc++.h>

using namespace std;

int n, k;
int a[6000];

void go(int i) {
    if (i > n) {
        for (int j = 1; j <= n; j++)
            cout << a[j] << ' ';
        cout << endl;
        return;
    }
    for (int j = a[i - 1] + 1; j <= k; j++) {
        a[i] = j;
        //cout << "!" << endl;
        go(i + 1);

    }
}

int main() {
    cin >> k >> n;
    go(1);
}
