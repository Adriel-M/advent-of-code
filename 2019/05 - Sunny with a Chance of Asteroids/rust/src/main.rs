use std::io;
use std::io::prelude::*;
use std::fs::File;

fn get_value(mode: i32, curr_value: i32, program: &mut Vec<i32>) -> i32 {
    if mode == 0 {
        return program[curr_value as usize];
    } else {
        return curr_value;
    }
}

fn execute_program(program: &mut Vec<i32>, insert_val: i32) -> i32 {
    let mut curr_index = 0;
    while program[curr_index] != 99 {
        let curr_instruction = program[curr_index];
        let opcode = curr_instruction % 10;
        let first_mode = (curr_instruction / 100) % 10;
        let first_val = get_value(first_mode, program[(curr_index + 1) as usize], program);
        match opcode {
            1 | 2 => {
                let second_mode = (curr_instruction / 1000) % 10;
                let second_val = get_value(second_mode, program[(curr_index + 2) as usize], program);
                let store_index = program[curr_index + 3] as usize;
                if opcode == 1 {
                    program[store_index] = first_val + second_val;
                } else {
                    program[store_index] = first_val * second_val;
                }
                curr_index += 4;
            },
            3 | 4 => {
                let pos_index = program[curr_index + 1] as usize;
                if opcode == 3 {
                    program[pos_index] = insert_val;
                } else {
                    if first_mode == 0 {
                        println!("{}", program[pos_index]);
                    } else {
                        println!("{}", pos_index);
                    }
                }
                curr_index += 2;
            },
            5 | 6 => {
                let second_mode = (curr_instruction / 1000) % 10;
                let second_val = get_value(second_mode, program[(curr_index + 2) as usize], program) as usize;
                if opcode == 5 && first_val != 0 {
                    curr_index = second_val;
                } else if opcode == 6 && first_val == 0 {
                    curr_index = second_val;
                } else {
                    curr_index += 3;
                }
            },
            7 | 8 => {
                let second_mode = (curr_instruction / 1000) % 10;
                let second_val = get_value(second_mode, program[(curr_index + 2) as usize], program);
                let mut to_store = 0;
                if opcode == 7 && first_val < second_val  {
                    to_store = 1;
                } else if opcode == 8 && first_val == second_val {
                    to_store = 1;
                }
                let store_index = program[curr_index + 3] as usize;
                program[store_index] = to_store;
                curr_index += 4;
            }
            _ => println!("Not supposed to be here!")
        }
    }
    return program[0];
}

fn main() -> io::Result<()> {
    let mut f = File::open("../input")?;
    let mut buffer = String::new();

    f.read_to_string(&mut buffer)?;

    let trimmed = buffer.trim();

    let program: Vec<i32> = trimmed.split(",").map(|s| s.parse().unwrap()).collect();

    println!("===== Part 1 ======");
    let mut program1 = program.clone();
    execute_program(&mut program1, 1);
    println!("===== Part 2 ======");
    let mut program2 = program.clone();
    execute_program(&mut program2, 5);

    Ok(())
}
