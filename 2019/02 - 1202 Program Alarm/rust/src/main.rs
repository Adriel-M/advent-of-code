use std::io;
use std::io::prelude::*;
use std::fs::File;

fn execute_program(program: &mut Vec<i32>) -> i32 {
    let mut curr_index = 0;
    while program[curr_index] != 99 {
        let target_index = program[curr_index + 3] as usize;
        let a_index = program[curr_index + 1] as usize;
        let b_index = program[curr_index + 2] as usize;
        match program[curr_index] {
            1 => {
                program[target_index] = program[a_index] + program[b_index];
            },
            2 => {
                program[target_index] = program[a_index] * program[b_index];
            },
            _ => println!("Not supposed to be here!")
        }
        curr_index += 4;
    }
    return program[0];
}

fn main() -> io::Result<()> {
    let mut f = File::open("../input")?;
    let mut buffer = String::new();

    f.read_to_string(&mut buffer)?;

    let trimmed = buffer.trim();

    let program: Vec<i32> = trimmed.split(",").map(|s| s.parse().unwrap()).collect();

    // part 1
    let mut program1 = program.clone();

    // 1202 program alarm state
    program1[1] = 12;
    program1[2] = 2;

    println!("===== Part 1 ======");
    println!("{}", execute_program(&mut program1));
    println!("===== Part 2 ======");

    'noun_loop: for noun in 0..100 {
        for verb in 0..100 {
            let mut copied = program.clone();
            copied[1] = noun;
            copied[2] = verb;
            if execute_program(&mut copied) == 19690720 {
                println!("{}", 100 * noun + verb);
                break 'noun_loop;
            }
        }
    }

    Ok(())
}
