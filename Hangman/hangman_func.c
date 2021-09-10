#include <stdio.h>
#include <stdlib.h>
#include <string.h>

//Find a better way to do this
void space() {
    for (int i = 0; i<100; i++) {
        printf("\n");
    }
};

//TODO: Implement this
int validWordCheck(char *input, int len) {
    for (int i = 0; i < len; i++) {
        int val = (int) input[i];
        if ((val < 65) || (val > 91 && val < 97) || (val > 122)) {return 1;}
    }
    return 0;
}

int checkSuccess(char *input, int len) {
    for (int i = 0; i < len; i++) {
        if (input[i] == '_') {
            return 0;
        }
    }
    return 1;
}

int bigCheck(char *blanks, char *input, int len, char check) {
    int retVal = 0;
    for (int i = 0; i < len; i++) {
        if (check == input[i] && check != blanks[i]) {
            retVal++;
            blanks[i] = input[i];
        }
    }
    return retVal;
}

int main(int argc, char *argv[]) {
    if (argc != 2) {
        printf("Usage: %s \"Word You Want to Play With\"\n", argv[0]);
        return 1;
    }
    int inputLen = strlen(argv[1]);                       
    if (validWordCheck(argv[1], inputLen)) {
        printf("\"%s\" is an invalid word. Use letters please!\n", argv[1]);
        return 1;
    }
    char stages[8][140] = {"  +------------+\n  |            |\n               |\n               |\n               |\n               |\n               |\n --------------+---\n",
                           "  +------------+\n  |            |\n  0            |\n               |\n               |\n               |\n               |\n --------------+---\n",
                           "  +------------+\n  |            |\n  0            |\n  +            |\n               |\n               |\n               |\n --------------+---\n",
                           "  +------------+\n  |            |\n  0            |\n  +            |\n  |            |\n               |\n               |\n --------------+---\n",
                           "  +------------+\n  |            |\n  0            |\n--+            |\n  |            |\n               |\n               |\n --------------+---\n",
                           "  +------------+\n  |            |\n  0            |\n--+--          |\n  |            |\n               |\n               |\n --------------+---\n",
                           "  +------------+\n  |            |\n  0            |\n--+--          |\n  |            |\n /             |\n               |\n --------------+---\n",
                           "  +------------+\n  |            |\n  0            |\n--+--          |\n  |            |\n / \\           |\n               |\n --------------+---\n"};
    char blanks[inputLen];
    for (int i = 0; i < inputLen; i++) {
        blanks[i] = '_';
    }
    char failChar[8] = "________";
    int success = 1;
    int fail = 0;
    while(fail < 7 && success) {
        space();
        for (int i = 0; i < 8; i++) {
            if (failChar[i] != '_') { printf("%c ", failChar[i]); }
        }
        printf("\n\n");
        printf("%s", stages[fail]);
        for (int i = 0; i < inputLen; i++) { printf("%c ", blanks[i]); }
        printf("\n\n");
        printf("Enter a character: ");

        char input = getchar();
        if (validWordCheck(&input, 1)) { continue; }
        
        int retVal = bigCheck(blanks, argv[1], inputLen, input);
        if (retVal == 0) { 
            failChar[fail] = input;
            fail++; 
        }
        if(checkSuccess(blanks, inputLen)) { success = 0; }
    }
    if(fail == 7) {
        space();
        for (int i = 0; i < 8; i++) {
            if (failChar[i] != '_') { printf("%c ", failChar[i]); }
        }
        printf("\n\n");
        printf("%s", stages[fail]);
        printf("You failed, try again?\n");
        return 1;
    }
    printf("Success! The word is \"%s\"\nPlay again?\n", argv[1]);
    return 0;
}