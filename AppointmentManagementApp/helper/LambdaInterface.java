/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package helper;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 *
 * @author Evan
 */
public interface LambdaInterface {
    Duration getDuration(LocalDateTime time1, LocalDateTime time2);
}
