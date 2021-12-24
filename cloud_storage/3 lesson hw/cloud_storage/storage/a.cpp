#include <bits/stdc++.h>

using namespace std;

int n, t, k;
bool u[100005];

int main() {
    for (int n = 1; n <= 1111; n++) {
        bool f = false;
        for (int i = 0; i <= n / 11; i++){
            for (int j = 0; j <= n / 111; j++) {
                if (i * 11 + j * 111 == n) {
                    f = true;
                    break;
                }
            }
        }
        if (!f) u[n] = true;
    }
    cin >> t;
    while (t--) {
        cin >> k;
        if (k > 1111) cout << "YES" << endl;
        else {
            if (u[k]) cout << "NO" << endl;
            else cout << "YES" << endl;
        }
    }
}

//2047
