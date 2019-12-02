use std::fs::File;
use std::io::{BufReader, BufRead, Error};

fn calc_fuel1(mass: i32) -> i32 {
    return (mass / 3) - 2;
}

fn calc_fuel2(mass: i32) -> i32 {
    let curr_required_fuel = calc_fuel1(mass);
    if curr_required_fuel <= 0 {
        return 0;
    } else {
        return curr_required_fuel + calc_fuel2(curr_required_fuel);
    }
}

fn main() -> Result<(), Error> {
    let path = "../input";

    let input = File::open(path)?;
    let buffered = BufReader::new(input);

    let mut total_fuel1 = 0;
    let mut total_fuel2 = 0;
    for line in buffered.lines() {
        let parsed_line = line.unwrap().parse().unwrap();
        total_fuel1 += calc_fuel1(parsed_line);
        total_fuel2 += calc_fuel2(parsed_line);
    }

    println!("===== Part 1 =====");
    println!("Total Fuel: {}", total_fuel1);
    println!("===== Part 2 =====");
    println!("Total Fuel: {}", total_fuel2);

    Ok(())
}