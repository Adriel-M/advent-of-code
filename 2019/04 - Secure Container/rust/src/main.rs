fn is_valid1(num: i32) -> bool {
    let mut prev = std::i32::MIN;
    let base: i32 = 10;
    let mut contains_adjacent = false;
    for i in 0..6 {
        let curr_num: i32 = (num / base.pow(5 - i)) % 10;
        if curr_num < prev {
            return false;
        }
        if prev == curr_num {
            contains_adjacent = true;
        }
        prev = curr_num;
    }
    return contains_adjacent;
}

fn is_valid2(num: i32) -> bool {
    let mut prev = std::i32::MIN;
    let base: i32 = 10;
    let mut contains_adjacent = false;
    let mut current_adjacent_count = 0;
    for i in 0..6 {
        let curr_num: i32 = (num / base.pow(5 - i)) % 10;
        if curr_num < prev {
            return false;
        }
        if prev == curr_num {
            if current_adjacent_count == 0 {
                current_adjacent_count = 2;
            } else {
                current_adjacent_count += 1;
            }
        } else {
            if current_adjacent_count == 2 {
                contains_adjacent = true;
            }
            current_adjacent_count = 0;
        }
        prev = curr_num;
    }
    return contains_adjacent || current_adjacent_count == 2;
}

fn main() {
    let mut possible1 = 0;

    for num in 246515..=739105 {
        if !is_valid1(num) {
            continue;
        }
        possible1 += 1;
    }

    println!("===== Part 1 =====");
    println!("{}", possible1);

    let mut possible2 = 0;

    for num in 246515..=739105 {
        if !is_valid2(num) {
            continue;
        }
        possible2 += 1;
    }

    println!("===== Part 2 =====");
    println!("{}", possible2);
}
