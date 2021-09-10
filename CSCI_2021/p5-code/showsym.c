// Template for parsing an ELF file to print its symbol table
// UPDATED: Tue Dec  8 03:27:18 PM CST 2020 
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <sys/mman.h>
#include <elf.h>

// The below macros help to prevent errors when doing pointer
// arithmetic. Adding an offset to a pointer is the most common
// operation here while the other macros may not be needed.

#define PTR_PLUS_BYTES(ptr,off) ((void *) (((size_t) (ptr)) + ((size_t) (off))))
// macro to add a byte offset to a pointer, arguments are a pointer
// and a # of bytes (usually size_t)

#define PTR_MINUS_BYTES(ptr,off) ((void *) (((size_t) (ptr)) - ((size_t) (off))))
// macro to subtract a byte offset from a pointer, arguments are a pointer
// and a # of bytes (usually size_t)

#define PTR_MINUS_PTR(ptr,ptq) ((long) (((size_t) (ptr)) - ((size_t) (ptq))))
// macro to subtract one pointer from another

int DEBUG = 0;                  
// Controls whether to print debug messages
// Can be used in conditionals like if(DEBUG){ ... }
// and running 'showsym -d x.o' will set DEBUG=1

int main(int argc, char *argv[]){
  if(argc < 2){
    printf("usage: %s [-d] <file>\n",argv[0]);
    return 0;
  }

  char *objfile_name = argv[1];

  // check for debug mode
  if(argc >=3){
    if(strcmp("-d",argv[1])==0){
      DEBUG = 1;
      objfile_name = argv[2];
    }
    else{
      printf("incorrect usage\n");
      return 1;
    }
  }
                        
  // Open file descriptor and set up memory map for objfile_name
  int fd = open(objfile_name, O_RDONLY);
  struct stat statistics;
  fstat(fd, &statistics);
  int size = statistics.st_size;
  char *mapping = mmap(NULL, size, PROT_READ, MAP_PRIVATE, fd, 0);

  // CREATE A POINTER to the intial bytes of the file which are an ELF64_Ehdr struct
  Elf64_Ehdr *ehdr = (Elf64_Ehdr *) mapping;


  // CHECK e_ident field's bytes 0 to for for the sequence {0x7f,'E','L','F'}.
  // Exit the program with code 1 if the bytes do not match
  int header_check = 
    ehdr->e_ident[0] == 0x7f &&
    ehdr->e_ident[1] == 'E' &&
    ehdr->e_ident[2] == 'L' &&
    ehdr->e_ident[3] == 'F';

  if(!header_check) {
    printf("ERROR: Magic bytes wrong, this is not an ELF file\n");
    exit(1);
  }

  // PROVIDED: check for a 64-bit file
  if(ehdr->e_ident[EI_CLASS] != ELFCLASS64){
    printf("Not a 64-bit file ELF file\n");
    return 1;
  }

  // PROVIDED: check for x86-64 architecture
  if(ehdr->e_machine != EM_X86_64){
    printf("Not an x86-64 file\n");
    return 1;
  }

  // DETERMINE THE OFFSET of the Section Header Array (e_shoff), the
  // number of sections (e_shnum), and the index of the Section Header
  // String table (e_shstrndx). These fields are from the ELF File
  // Header.
  int offset = ehdr->e_shoff;
  int num_sections = ehdr->e_shnum;
  int sec_head_offset = ehdr->e_shstrndx;

  // Set up a pointer to the array of section headers. Use the section
  // header string table index to find its byte position in the file
  // and set up a pointer to it.
  Elf64_Shdr *sec_hdrs = (Elf64_Shdr *) (mapping + offset);
  char *sec_names = (char *) (mapping + sec_hdrs[sec_head_offset].sh_offset);
  

  // Search the Section Header Array for the secion with name .symtab
  // (symbol table) and .strtab (string table).  Note their positions
  // in the file (sh_offset field).  Also note the size in bytes
  // (sh_size) and and the size of each entry (sh_entsize) for .symtab
  // so its number of entries can be computed.
  long int sym_offset = 0;
  long int str_offset = 0;
  long int sym_size = 0;
  long int sym_entry = 0;
  long int symtab_num = 0;

  for(int i=0; i<num_sections; i++){
    int cmp_str = strcmp(".strtab", (sec_names + sec_hdrs[i].sh_name));
    int cmp_sym = strcmp(".symtab", (sec_names + sec_hdrs[i].sh_name));
    if(!cmp_str) {
      str_offset = sec_hdrs[i].sh_offset;
    }
    else if (!cmp_sym) {
      sym_offset = sec_hdrs[i].sh_offset;
      sym_size = sec_hdrs[i].sh_size;
      sym_entry = sec_hdrs[i].sh_entsize;
      symtab_num = sym_size/sym_entry;
    }
  }

  if(!sym_offset){
    printf("ERROR: Couldn't find symbol table\n");
    return 1;
  }
  if(!str_offset){
    printf("ERROR: Couldn't find string table\n");
    return 1;
  }

  // PRINT byte information about where the symbol table was found and
  // its sizes. The number of entries in the symbol table can be
  // determined by dividing its total size in bytes by the size of
  // each entry.
  printf("Symbol Table\n");
  printf("- %ld bytes offset from start of file\n",sym_offset);
  printf("- %ld bytes total size\n",sym_size);
  printf("- %ld bytes per entry\n",sym_entry);
  printf("- %ld entries\n",symtab_num);

  // Set up pointers to the Symbol Table and associated String Table
  // using offsets found earlier.
  Elf64_Sym *sym_struct = (Elf64_Sym *) (mapping + sym_offset);
  char *str_table = (char *) (mapping + str_offset);

  // Print column IDs for info on each symbol
  printf("[%3s]  %8s %4s %s\n",
         "idx","TYPE","SIZE","NAME");

  // Iterate over the symbol table entries
  for(int i=0; i<symtab_num; i++){
    // Determine size of symbol and name. Use <NONE> name has zero
    // length.
    long unsigned int entry_size = sym_struct[i].st_size;
    int entry_name = sym_struct[i].st_name;
    char *name;
    if (!entry_name) { name = "<NONE>"; }
    else { name = (str_table + entry_name); }
    // Determine type of symbol. See assignment specification for
    // fields, macros, and definitions related to this.
    unsigned char typec = ELF64_ST_TYPE(sym_struct[i].st_info);
    char *type = "";
    if (typec == STT_NOTYPE) { type = "NOTYPE"; }
    else if (typec == STT_OBJECT) { type = "OBJECT"; }
    else if (typec == STT_FUNC) { type = "FUNC"; }
    else if (typec == STT_FILE) { type = "FILE"; }
    else if (typec == STT_SECTION) { type = "SECTION"; }
    // Print symbol information
    printf("[%3d]: %8s %4lu %s\n",i,type,entry_size,name);
  }

  // Unmap file from memory and close associated file descriptor 
  munmap(mapping, size);
  close(fd);
  return 0;
}
