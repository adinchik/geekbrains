#include <iostream>

using namespace std;

string s;
int o[200000], z[200000];

int main()
{
    freopen("a.in", "r", stdin);
    cin >> s;
    if (s[0] == '0') z[1] = 1;
    else o[1] = 1;
    for (int i = 1; i < s.size(); i++) {
        if (s[i] == '0') {
            z[i + 1] = z[i] + 1;
            o[i + 1] = o[i];
        }
        else {
            o[i + 1] = o[i] + 1;
            z[i + 1] = z[i];
        }
    }
    int ans = 2e9;
    int n = s.size();
    s += '2';
    int st = 1;
    for (int i = 0; i < s.size() - 1; i++) {
        if (s[i] == s[i + 1]) {
            continue;
        }
        else {
            //cout << ans << ' ' << st << endl;
            ans = min(ans, o[st - 1] + z[n] - z[i + 1]);
            st = i + 2;
        }
    }
    cout << ans << endl;

}
