import React, { useEffect } from 'react';
import {
    View,
    Text,
    TextInput,
    TouchableOpacity,
    StyleSheet,
    ScrollView,
    Alert,
    ActivityIndicator
} from 'react-native';
import { useForm, Controller } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import * as yup from 'yup';
import Toast from 'react-native-toast-message';

import axiosSetup from '../api/axiosSetup';

const schema = yup.object({
    login: yup.string().required('Login wymagany'),
    firstName: yup.string().required('Imię wymagane'),
    lastName: yup.string().required('Nazwisko wymagane'),
    email: yup.string().email('Błędny email').required('Email wymagany'),
    password: yup.string().test('req', 'Hasło wymagane', function (val) {
        if (!this.options.context?.isEdit) return !!val && val.length >= 5;
        return true;
    }),
});

export default function UserForm({ route, navigation }: any) {
    const { id } = route.params || {};
    const isEdit = !!id;

    const { control, handleSubmit, setValue, formState: { errors } } = useForm({
        resolver: yupResolver(schema),
        context: { isEdit },
        defaultValues: {
            login: '',
            firstName: '',
            lastName: '',
            email: '',
            password: ''
        }
    });

    useEffect(() => {
        if (isEdit) {
            axiosSetup.get(`/users/${id}`).then(res => {
                const u = res.data;
                setValue('login', u.login);
                setValue('firstName', u.firstName);
                setValue('lastName', u.lastName);
                setValue('email', u.email);
            }).catch(err => {
                Toast.show({ type: 'error', text1: 'Błąd pobierania danych' });
            });
        }
    }, [id]);

    const onFormSubmit = (data: any) => {
        Alert.alert(
            "Potwierdzenie",
            "Zapisać zmiany?",
            [
                { text: "Anuluj", style: "cancel" },
                {
                    text: "OK",
                    onPress: () => processSubmit(data)
                }
            ]
        );
    };

    const processSubmit = async (data: any) => {
        const payload = { ...data, isActive: true };

        try {
            if (isEdit) {
                await axiosSetup.put(`/users/${id}`, payload);
            } else {
                await axiosSetup.post('/users/client', payload);
            }

            Toast.show({
                type: 'success',
                text1: 'Sukces',
                text2: isEdit ? 'Zaktualizowano dane' : 'Dodano użytkownika'
            });

            navigation.goBack();
        } catch (e: any) {
            const msg = e.response?.data?.message || "Błąd zapisu";
            Toast.show({ type: 'error', text1: 'Błąd', text2: msg });
        }
    };

    const renderInput = (name: any, label: string, placeholder: string, secure = false, disabled = false) => (
        <View style={styles.inputContainer}>
            <Text style={styles.label}>{label}</Text>
            <Controller
                control={control}
                name={name as any}
                render={({ field: { onChange, onBlur, value } }) => (
                    <TextInput
                        style={[styles.input, disabled && styles.inputDisabled]}
                        onBlur={onBlur}
                        onChangeText={onChange}
                        value={value}
                        placeholder={placeholder}
                        secureTextEntry={secure}
                        editable={!disabled}
                        autoCapitalize="none"
                    />
                )}
            />
            {(errors as any)[name] && (
                <Text style={styles.errorText}>
                    {(errors as any)[name]?.message as string}
                </Text>
            )}
        </View>
    );

    return (
        <ScrollView contentContainerStyle={styles.container}>
            <Text style={styles.header}>
                {isEdit ? 'Edytuj dane użytkownika' : 'Dodaj użytkownika'}
            </Text>

            {renderInput('login', 'Login', 'Wpisz login', false, isEdit)}
            {renderInput('firstName', 'Imię', 'Wpisz imię')}
            {renderInput('lastName', 'Nazwisko', 'Wpisz nazwisko')}
            {renderInput('email', 'Email', 'Wpisz email')}

            {!isEdit && renderInput('password', 'Hasło', 'Wpisz hasło', true)}

            <TouchableOpacity
                style={styles.button}
                onPress={handleSubmit(onFormSubmit)}
            >
                <Text style={styles.buttonText}>
                    {isEdit ? 'Zapisz zmiany' : 'Dodaj użytkownika'}
                </Text>
            </TouchableOpacity>
        </ScrollView>
    );
}

const styles = StyleSheet.create({
    container: {
        padding: 20,
        backgroundColor: '#f5f5f5',
        flexGrow: 1,
    },
    header: {
        fontSize: 24,
        fontWeight: 'bold',
        marginBottom: 20,
        color: '#333',
        textAlign: 'center',
    },
    inputContainer: {
        marginBottom: 15,
    },
    label: {
        marginBottom: 5,
        fontSize: 16,
        fontWeight: '500',
        color: '#555',
    },
    input: {
        backgroundColor: 'white',
        borderWidth: 1,
        borderColor: '#ddd',
        borderRadius: 8,
        padding: 12,
        fontSize: 16,
    },
    inputDisabled: {
        backgroundColor: '#e0e0e0',
        color: '#888',
    },
    errorText: {
        color: 'red',
        fontSize: 14,
        marginTop: 4,
    },
    button: {
        marginTop: 20,
        backgroundColor: '#007AFF',
        paddingVertical: 15,
        borderRadius: 8,
        alignItems: 'center',
    },
    buttonText: {
        color: 'white',
        fontSize: 18,
        fontWeight: 'bold',
    },
});