'use client'
import styles from "../component/header.module.css";
import React, { useState } from "react";
import { Modal, Box, Typography, Button, Grid, TextField } from "@mui/material";

const style = {
    position: 'absolute' as const,
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 500,
    bgcolor: '#87C75C',
    border: '2px solid #0B540D',
    borderRadius: '8px',
    boxShadow: 24,
    p: 4,
    display: 'flex',
    flexDirection: 'column' as const,
    justifyContent: 'center',
    alignItems: 'center',
};

const modalStyle = {
    backgroundColor: 'white',
    color: '#4CAF50',
    cursor: 'pointer',
};

const inputStyle = {
    width: '100%',
    marginBottom: '16px',
};

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
                className={styles.button}
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
                <Box sx={style}>
                    <Typography
                        id='modal-modal-title'
                        variant='h6'
                        component='h2'
                        sx={{
                            marginBottom: 2,
                            fontWeight: 600,
                        }}
                    >
                        {isSignup ? 'Create an account' : 'Log in to your account'}
                    </Typography>

                    <Grid container spacing={2} justifyContent='center'>
                        {isSignup ? (
                            <>
                                <Grid item xs={6}>
                                    <TextField
                                        label='Username'
                                        variant='filled'
                                        fullWidth
                                        required
                                        value={username}
                                        onChange={e => setUsername(e.target.value)}
                                        InputProps={{
                                            style: {
                                                backgroundColor: '#ffffff',
                                                border: '2px solid #0B540D',
                                                borderRadius: '8px',
                                            },
                                        }}
                                        InputLabelProps={{
                                            style: {
                                                color: '#0B540D',
                                                fontWeight: 600,
                                            },
                                        }}
                                    />
                                </Grid>

                                <Grid item xs={6}>
                                    <TextField
                                        label='Email'
                                        variant='filled'
                                        fullWidth
                                        required
                                        value={email}
                                        onChange={e => setEmail(e.target.value)}
                                        InputProps={{
                                            style: {
                                                backgroundColor: '#ffffff',
                                                border: '2px solid #0B540D',
                                                borderRadius: '8px',
                                            },
                                        }}
                                        InputLabelProps={{
                                            style: {
                                                color: '#0B540D',
                                                fontWeight: 600,
                                            },
                                        }}
                                    />
                                </Grid>
                            </>
                        ) : (
                            <Grid item xs={6}>
                                <TextField
                                    label='Username/Email'
                                    variant='filled'
                                    fullWidth
                                    required
                                    value={email} // Use email for both Sign Up and Sign In
                                    onChange={e => setEmail(e.target.value)} // Update email value only
                                    InputProps={{
                                        style: {
                                            backgroundColor: '#ffffff',
                                            border: '2px solid #0B540D',
                                            borderRadius: '8px',
                                        },
                                    }}
                                    InputLabelProps={{
                                        style: {
                                            color: '#0B540D',
                                            fontWeight: 600,
                                        },
                                    }}
                                />
                            </Grid>
                        )}

                        {/* Password Input */}
                        <Grid item xs={4}>
                            <TextField
                                label='Password'
                                variant='filled'
                                fullWidth
                                required
                                type='password'
                                value={password}
                                onChange={e => setPassword(e.target.value)}
                                InputProps={{
                                    style: {
                                        backgroundColor: '#ffffff',
                                        border: '2px solid #0B540D',
                                        borderRadius: '8px',
                                    },
                                }}
                                InputLabelProps={{
                                    style: {
                                        color: '#0B540D',
                                        fontWeight: 600,
                                    },
                                }}
                            />
                        </Grid>
                    </Grid>

                    <Button
                        variant='contained'
                        sx={{
                            backgroundColor: '#ffffff',
                            color: '#0B540D',
                            border: '2px solid #0B540D',
                            marginTop: 2,
                            fontWeight: 600,
                            '&:hover': {
                                backgroundColor: '#6CA047',
                                borderColor: '#0B540D',
                                boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
                            },
                        }}
                    >
                        {isSignup ? 'Sign up' : 'Sign in'}
                    </Button>

                    <Button
                        variant='text'
                        fullWidth
                        sx={{
                            marginTop: 2,
                            color: '#0B540D',
                            fontWeight: 600,
                        }}
                        onClick={handleToggleForm}
                    >
                        {isSignup ? 'Already have an account?' : "Don't have an account?"}
                    </Button>
                </Box>
            </Modal>
        </div>
    );
}