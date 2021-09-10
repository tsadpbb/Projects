// treemap_funcs.c: Provides a small library of functions that operate on
// binary search trees mapping strings keys to string values.
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "treemap.h"

void treemap_init(treemap_t *tree) {
    tree->root = NULL;
    tree->size = 0;                 // Simple init
}
// Initialize the given tree to have a null root and have size 0.

int treemap_add(treemap_t *tree, char key[], char val[]){
    node_t *ptr = tree->root;
    node_t *newNode = malloc(sizeof(node_t));
    strcpy(newNode->key, key);                      //This chunk makes a node for the while loop
    strcpy(newNode->val, val);
    newNode->left = NULL;
    newNode->right = NULL;
    tree->size++;
    while (ptr != NULL) {
        int check = strcmp(key, ptr->key);
        if (check < 0) {
            if (ptr->left != NULL) { ptr = ptr->left; }         // Go down the left branch if less than the key
            else {
                ptr->left = newNode;                            //If the left doesn't exist, it places the new node
                return 1;
            }
        }
        else if (check > 0) {
            if (ptr->right != NULL) { ptr = ptr->right; }       // Go down the left branch if greater than key
            else {
                ptr->right = newNode;                           // If the right doesn't exist, it places the new node
                return 1;
            }
        }
        else {
            strcpy(ptr->val, val);           // If the key already exists, it resets the value and frees the unused node
            free(newNode);
            tree->size--;
            return 0;
        }
    }
    tree->root = newNode;           // This is if the root points to NULL
    return 1;
}
// Inserts given key/value into a binary search tree. Uses an
// ITERATIVE (loopy) approach to insertion which starts a pointer at
// the root of the tree and changes its location until the correct
// insertion point is located. If the key given already exists in the
// tree, no new node is created; the existing value is changed to the
// parameter 'val' and 0 is returned.  If no node with the given key
// is found, a new node is created and with the given key/val, added
// to the tree, and 1 is returned. Makes use of strcpy() to ease
// copying characters between memory locations.

char *treemap_get(treemap_t *tree, char key[]) {
    node_t *newNode = tree->root;                   //Traverses similar to treemap_add
    while (newNode != NULL) {                       //If the key doesn't exist, newNode gets pointed to
        int check = strcmp(key, newNode->key);      //A NULL pointer and the while loop stops, which just leads to
        if (check < 0) {
            newNode = newNode->left;
        }
        else if (check > 0) {
            newNode = newNode->right;
        }
        else {
            return  newNode->val;
        }
    }
    return NULL;                                //This
}
// Searches the tree for given 'key' and returns its associated
// value. Uses an ITERATIVE (loopy) search approach which starts a
// pointer at the root of the tree and changes it until the search key
// is found or determined not to be in the tree. If a matching key is
// found, returns a pointer to its value. If no matching key is found,
// returns NULL.

void treemap_clear(treemap_t *tree) {
    node_remove_all(tree->root);
    treemap_init(tree);
}
// Eliminate all nodes in the tree setting its contents empty. Uses
// recursive node_remove_all() function to free memory for all nodes.

void node_remove_all(node_t *cur) {
    if (cur != NULL) {
        node_remove_all(cur->left);
        node_remove_all(cur->right);
        free(cur);
    }
}
// Recursive helper function which visits all nodes in a tree and
// frees the memory associated with them. This requires a post-order
// traversal: visit left tree, visit right tree, then free the cur
// node.

void treemap_print_revorder(treemap_t *tree) {
    node_print_revorder(tree->root, 0);
}
// Prints the key/val pairs of the tree in reverse order at differing
// levels of indentation which shows all elements and their structure
// in the tree. Visually the tree can be rotated clockwise to see its
// structure. See the related node_print_revorder() for additional
// detals.

void node_print_revorder(node_t *cur, int indent){
    if (cur != NULL) {
        node_print_revorder(cur->right, indent+1);
        for (int i = 0; i < indent; i++) {              //Adds the indents to the front of the string stuff
            printf("  ");
        }
        printf("%s -> %s\n", cur->key, cur->val);       //Prints the key and val
        node_print_revorder(cur->left, indent+1);
    }
}
// Recursive helper function which prints all key/val pairs in the
// tree rooted at node 'cur' in reverse order. Traverses right
// subtree, prints cur node's key/val, then traverses left tree.
// Parameter 'indent' indicates how far to indent (2 spaces per indent
// level).
//
// For example: a if the root node "El" is passed into the function
// and it has the following structure:
// 
//         ___El->strange_____     
//        |                   |   
// Dustin->corny       ___Mike->stoic
//                    |              
//               Lucas->brash     
// 
// the recursive calls will print the following output:
// 
//   Mike -> stoic                 # root->right
//     Lucas -> brash              # root->right->left
// El -> strange                   # root
//   Dustin -> corny               # root->left

void treemap_print_preorder(treemap_t *tree) {
    node_write_preorder(tree->root, stdout, 0);
}
// Print all the data in the tree in pre-order with indentation
// corresponding to the depth of the tree. Makes use of
// node_write_preorder() for this.

void treemap_save(treemap_t *tree, char *fname) {
    FILE *fp = fopen(fname, "w+");
    node_write_preorder(tree->root, fp, 0);
    fclose(fp);
}
// Saves the tree by opening the named file, writing the tree to it in
// pre-order with node_write_preorder(), then closing the file.

void node_write_preorder(node_t *cur, FILE *out, int depth) {
    if (cur != NULL) {
        for (int i = 0; i < depth; i++) {
            fprintf(out, "  ");
            fflush(out);
        }
        fprintf(out, "%s %s\n", cur->key, cur->val );
        node_write_preorder(cur->left, out, depth+1);
        node_write_preorder(cur->right, out, depth+1);
    }
}
// Recursive helper function which writes/prints the tree in pre-order
// to the given open file handle. The parameter depth gives how far to
// indent node data, 2 spaces per unit depth. Depth increases by 1 on
// each recursive call. The function prints the cur node data,
// traverses the left tree, then traverses the right tree.

int treemap_load(treemap_t *tree, char *fname ) {
    FILE *fp = fopen(fname, "r");
    if (fp == NULL) {
        printf("ERROR: could not open file '%s'\n", fname);     //Checks if everything opened fine
        return 0;
    }
    treemap_clear(tree);
    char key[25], val[25];
    while (fscanf(fp, "%s %s", key, val) != EOF) {      //interates through the file and adds using treemap_add
        treemap_add(tree, key, val);
    }
    fclose(fp);
    return 1;
}
// Clears the given tree then loads new elements to it from the
// named. Repeated calls to treemap_add() are used to add strings read
// from the file.  If the tree is stored in pre-order in the file, its
// exact structure will be restored.  Returns 1 if the tree is loaded
// successfully and 0 if opening the named file fails in which case no
// changes are made to the tree.
