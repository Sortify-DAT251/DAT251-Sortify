'use client'
import React, { useState } from "react";
import { Modal, Box, Typography, Button, Grid, TextField } from "@mui/material";

const style = {
    position: 'absolute' as const,
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    width: 500,
    bgcolor: '#87C75C',
    border: '2px solid #000',
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
                className="button"
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
                        sx={{ marginBottom: 2 }}
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
                                                border: '2px solid green',
                                                borderRadius: '8px',
                                            },
                                        }}
                                        InputLabelProps={{
                                            style: {
                                                color: 'darkgreen',
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
                                                border: '2px solid green',
                                                borderRadius: '8px',
                                            },
                                        }}
                                        InputLabelProps={{
                                            style: {
                                                color: 'darkgreen',
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
                                            border: '2px solid green',
                                            borderRadius: '8px',
                                        },
                                    }}
                                    InputLabelProps={{
                                        style: {
                                            color: 'darkgreen',
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
                                        border: '2px solid green',
                                        borderRadius: '8px',
                                    },
                                }}
                                InputLabelProps={{
                                    style: {
                                        color: 'darkgreen',
                                    },
                                }}
                            />
                        </Grid>
                    </Grid>

                    <Button
                        variant='contained'
                        sx={{
                            backgroundColor: '#ffffff',
                            color: 'black',
                            border: '2px solid black',
                            marginTop: 2,
                            '&:hover': {
                                backgroundColor: '#87C75C',
                                borderColor: '#000000',
                                boxShadow: '0 4px 6px rgba(0, 0, 0, 0.1)',
                            },
                        }}
                    >
                        {isSignup ? 'Sign up' : 'Sign in'}
                    </Button>

                    <Button
                        variant='text'
                        color='secondary'
                        fullWidth
                        sx={{ marginTop: 2 }}
                        onClick={handleToggleForm}
                    >
                        {isSignup ? 'Already have an account?' : "Don't have an account?"}
                    </Button>
                </Box>
            </Modal>
        </div>
    );
}