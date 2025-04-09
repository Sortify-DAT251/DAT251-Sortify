'use client'
import styles from "../styling/signup.module.css";
import React, { useState } from "react";
import { Modal, Box, Typography, Button, Grid, TextField } from "@mui/material";

export default function SignupModal() {
    const [open, setOpen] = useState<boolean>(false);
    const [isSignup, setIsSignup] = useState<boolean>(false);
    const [username, setUsername] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleOpen = () => setOpen(true);
    const handleClose = () => setOpen(false);
    const handleToggleForm = () => setIsSignup(!isSignup);

    return (
        <div>
            <a
                className={styles.signupButton}
                onClick={handleOpen}
            >
                Sign Up / In
            </a>
            <Modal
                open={open}
                onClose={handleClose}
                aria-labelledby='modal-modal-title'
                aria-describedby='modal-modal-description'
            >
                <Box className={styles.modalBox}>
                    <Typography
                        id='modal-modal-title'
                        variant='h6'
                        component='h2'
                        sx={{ marginBottom: 2, fontWeight: 600 }}
                    >
                        {isSignup ? 'Create an account' : 'Log in to your account'}
                    </Typography>

                    <Grid container spacing={2} justifyContent='center'>
                        {isSignup ? (
                            <>
                                <Grid item xs={6}>
                                    <TextField
                                        className={styles.formField}
                                        label='Username'
                                        variant='filled'
                                        fullWidth
                                        required
                                        value={username}
                                        onChange={e => setUsername(e.target.value)}
                                    />
                                </Grid>
                                <Grid item xs={6}>
                                    <TextField
                                        className={styles.formField}
                                        label='Email'
                                        variant='filled'
                                        fullWidth
                                        required
                                        value={email}
                                        onChange={e => setEmail(e.target.value)}
                                    />
                                </Grid>
                            </>
                        ) : (
                            <Grid item xs={6}>
                                <TextField
                                    className={styles.formField}
                                    label='Username/Email'
                                    variant='filled'
                                    fullWidth
                                    required
                                    value={email}
                                    onChange={e => setEmail(e.target.value)}
                                />
                            </Grid>
                        )}

                        <Grid item xs={4}>
                            <TextField
                                className={styles.formField}
                                label='Password'
                                variant='filled'
                                fullWidth
                                required
                                type='password'
                                value={password}
                                onChange={e => setPassword(e.target.value)}
                            />
                        </Grid>
                    </Grid>

                    <Button className={styles.actionButton}>
                        {isSignup ? 'Sign up' : 'Sign in'}
                    </Button>

                    <Button
                        className={styles.toggleButton}
                        variant='text'
                        fullWidth
                        onClick={handleToggleForm}
                    >
                        {isSignup ? 'Already have an account?' : "Don't have an account?"}
                    </Button>
                </Box>
            </Modal>
        </div>
    );
}
