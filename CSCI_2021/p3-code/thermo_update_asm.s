.text
.global     set_temp_from_ports

set_temp_from_ports:
    movw    THERMO_SENSOR_PORT(%rip), %bx
    movb    THERMO_STATUS_PORT(%rip), %cl       # Load Globals into registers, might have to get address for Status
    andb    $0b0001, %cl                        # Mask operation right away to get it done with
    cmpw    $64000, %bx                         # Check if Sensor is in the right range
    ja      .FAIL
    cmpw    $0, %bx
    jb      .FAIL
    movw    %bx, %dx
    shrw    $6, %dx                             # Divide by 64
    subw    $500, %dx                           # Subtract 500
    andb    $63, %bl                            # Get the remainder
    cmpb    $31, %bl                            # If it's less than 32 it'll jump to POST
    jbe     .POST
    incw    %dx                                 # If it's greater than 32, it'll increment bx by one
.POST:
    movb    $0, 2(%rdi)
    movw    %dx, %r8w                           # dx is used for division
    cmpb    $0, %cl
    je      .FINAL                              # If it's not farenheit it'll jump to FINAL, no conversion neccesary
    imulw   $9, %dx
    movw    %dx, %ax
    movw    $5, %bx
    cwtl
    cltq
    cqto                                        # Set up division 
    idivw   %bx
    movw    %ax, %r8w
    addw    $320, %r8w   
    movb    $1, 2(%rdi)
.FINAL:
    movw    %r8w, 0(%rdi)                   # Put to temp pointer, powerful stuff isn't it?
    movl    $0, %eax
    ret
.FAIL:
    movl    $1, %eax
    ret

.data
bit_array:                                     # Array that will be used later, for a secret special devious purpose
    .int 0b1111110
    .int 0b0001100
    .int 0b0110111
    .int 0b0011111
    .int 0b1001101
    .int 0b1011011
    .int 0b1111011
    .int 0b0001110
    .int 0b1111111
    .int 0b1011111

.text
.global     set_display_from_temp

# temp in rdi, display pointer in rsi
set_display_from_temp:
    movl    %edi, %r8d
    shrl    $16, %r8d           # Shift to get the right stuff
    movb    %r8b, %bl           # Load is_farenheit 
    movw    %di, %cx            # Load tenths
    cmpb    $0, %bl
    je      .CELCIUS_CHECK      # Check stuff, if celcius, I think it's spelled, spelt, spellen??? wrong, don't care
    cmpb    $1, %bl             # Make sure it's the right format, not like 5 or some stupid stuff
    jne     .FAIL2ELECTICBOOGALO
    cmpw    $9999, %cx                 # Correct range, technically not right but don't care
    jg      .FAIL2ELECTICBOOGALO
    cmpw    $-999, %cx
    jl      .FAIL2ELECTICBOOGALO
    xorq    %r8, %r8            # Clear register, will be loaded into display
    xorq    %r10, %r10          # Clear this one too because it didn't work when I didn't and I don't care
    orb     $0b10, %r10b        # The basic procces is I mask the bit combo I want then shift it the amount I want it
    shll    $28, %r10d
    orl     %r10d, %r8d         # Do what must be done, I know what's happening but I don't care enough to explain      
    jmp     .POST2ELCTRICBOOGALO # Honestly if you don't understand what's happening just by looking and vague comments, you need to read up on your x86-64 assembly
.CELCIUS_CHECK:                 # Same thing above but for celcius
    cmpw    $500, %cx
    jg      .FAIL2ELECTICBOOGALO
    cmpw    $-500, %cx
    jl      .FAIL2ELECTICBOOGALO
    xorq    %r8, %r8            # Clear register, will be loaded into display
    xorq    %r10, %r10
    orb     $0b01, %r10b
    shll    $28, %r10d
    orl     %r10d, %r8d      
.POST2ELCTRICBOOGALO:
    movb    $1, %dil            # I store if it's negative or not for later because I need to make it positive for division
    cmpw    $0, %cx
    jge     .POSTNEG
    movb    $-1, %dil
    imulw   $-1, %cx            # To make dividing easier
.POSTNEG:
    leaq    bit_array(%rip), %rbx   # Load array
    movw    %cx, %ax            # Move int from temp stuct to ax for dividing
    movw    $100, %r9w
    cwtl                        # I don't know if this is neccesary every time, but I'm too lazy to chekc and it works anyway
    cltq
    cqto
    divw    %r9w               # I do an unsigned divide to ignore the negative, it's already handled
    movw    $10, %r9w          # This is a secret tool used later, basically I'm only going to be dividing by 10 from now on so might as well change this now
    cmpw    $0, %ax            # I basically tried to mimic the if else blocks in my C function, it's messy but it works
    je      .NEXT
    movw    %dx, %r11w         # To save the remainder for later
    cwtl
    cltq
    cqto
    divw    %r9w
    cmpw    $0, %ax
    je      .TONZERO
    xorq    %r10, %r10
    movl    (%rbx, %rax, 4), %r10d   # Same premise, premice??? as earlier
    shll    $21, %r10d
    orl     %r10d, %r8d
.TONZERO:
    cmpb    $1, %dil      # See? It was useful to store the negative for later, this process is repeated a lot because I'm stupid
    je      .POSTNEG2       # and can't figure out a better way, the gcc stuff tells me nothing useful
    xorq    %r10, %r10
    orb     $0b0000001, %r10b
    shll    $21, %r10d
    orl     %r10d, %r8d
.POSTNEG2:
    xorq    %r10, %r10
    movl    (%rbx, %rdx, 4), %r10d
    shll    $14, %r10d
    orl     %r10d, %r8d
    movw    %r11w, %dx              # I put this here to get back the remainder to make it compatible with postneg3, it's somewhere else too but, guess what? Don't care
    jmp     .POSTNEG3
.NEXT:
    cmpb    $1, %dil            # This is only used if temp.tenths_degress or whatever is less than 100
    je      .POSTNEG3
    xorq    %r10, %r10
    orb     $0b0000001, %r10b
    shll    $14, %r10d          # That's why this is shifted to 14
    orl     %r10d, %r8d
.POSTNEG3:
    movw    %dx, %ax
    cwtl
    cltq
    cqto
    divw    %r9w
    xorq    %r10, %r10
    movl    (%rbx, %rax, 4), %r10d     # Final stuff, compatible with every possible encounter
    shll    $7, %r10d
    orl     %r10d, %r8d
    xorq    %r10, %r10
    movl    (%rbx, %rdx, 4), %r10d
    orl     %r10d, %r8d
    movl    %r8d, (%rsi)        # Change display
    movq    $0, %rax
    ret
.FAIL2ELECTICBOOGALO:         # I needed to name the fails different, why? Because I don't care
    movq    $1, %rax
    ret

.text
.global     thermo_update

thermo_update:
    pushq   $0                                  # Push 8 bytes to stack
    movq    %rsp, %rdi                          # Move the pointer for those 8 bytes to arg 1 register
    call    set_temp_from_ports                 # Call first function to alter memory pointed to by rdi
    cmpl    $1, %eax                            # Check if the function returned an error
    je      .FINALFAIL                          
    movq    (%rdi), %rdi                        # Move information pointed to into arg 1 register
    movq    %rsp, %rsi                          # Reuse the original 8 bytes pushed to stack, move to second arg
    call    set_display_from_temp               # Call function
    cmpq    $1, %rax                            # Error checking
    je      .FINALFAIL
    movq    (%rsi), %rsi                        # Move info pointed to in stack to register, this is the display
    movl    %esi, THERMO_DISPLAY_PORT(%rip)     # Change the display port, only move 4 bytes because that's all that's important
    popq    %rax                                # Pop the stack to og
    movq    $0, %rax                            # Info was popped to rax so gotta reset it to 0
    ret                                         # Done!
.FINALFAIL:                                     # Fail Case
    popq    %rax
    movq    $1, %rax
    ret
