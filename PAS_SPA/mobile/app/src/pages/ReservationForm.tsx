import React, { useEffect, useState } from 'react';
import {
    View,
    Text,
    StyleSheet,
    TouchableOpacity,
    ScrollView,
    Alert,
    Platform
} from 'react-native';
import { useForm, Controller } from 'react-hook-form';
import { Picker } from '@react-native-picker/picker';
import DateTimePicker from '@react-native-community/datetimepicker';
import Toast from 'react-native-toast-message';
import axiosSetup from '../api/axiosSetup';

export default function ReservationForm({ navigation }: any) {
    const { control, handleSubmit, setValue, watch } = useForm();
    const [clients, setClients] = useState<any[]>([]);
    const [rooms, setRooms] = useState<any[]>([]);

    const [showDatePicker, setShowDatePicker] = useState(false);


    const selectedDate = watch('startTime');

    useEffect(() => {

        axiosSetup.get('/users')
            .then(res => setClients(res.data))
            .catch(() => Toast.show({ type: 'error', text1: "Błąd pobierania użytkowników" }));

        axiosSetup.get('/rooms')
            .then(res => setRooms(res.data))
            .catch(() => Toast.show({ type: 'error', text1: "Błąd pobierania boisk" }));
    }, []);

    const onFormSubmit = (data: any) => {
        Alert.alert(
            "Potwierdzenie",
            "Czy potwierdzasz rezerwację?",
            [
                { text: "Anuluj", style: "cancel" },
                { text: "Tak", onPress: () => processReservation(data) }
            ]
        );
    };

    const processReservation = async (data: any) => {
        try {
            const payload = {
                ...data,
                startTime: data.startTime ? new Date(data.startTime).toISOString() : null
            };

            await axiosSetup.post('/reservations', payload);

            Toast.show({ type: 'success', text1: "Rezerwacja dodana" });
            navigation.navigate('ReservationList');
        } catch (e: any) {
            console.error(e);
            const msg = e.response?.data?.message || "Wystąpił błąd podczas rezerwacji";
            Toast.show({ type: 'error', text1: "Błąd", text2: msg });
        }
    };

    const onDateChange = (event: any, selectedDate?: Date) => {
        setShowDatePicker(false);
        if (selectedDate) {
            setValue('startTime', selectedDate);
        }
    };

    return (
        <ScrollView contentContainerStyle={styles.container}>
            <Text style={styles.header}>Stwórz nową rezerwację</Text>

            <View style={styles.group}>
                <Text style={styles.label}>Wybierz klienta</Text>
                <View style={styles.pickerContainer}>
                    <Controller
                        control={control}
                        name="clientId"
                        rules={{ required: true }}
                        render={({ field: { onChange, value } }) => (
                            <Picker
                                selectedValue={value}
                                onValueChange={onChange}
                                style={styles.picker}
                            >
                                <Picker.Item label="-- Wybierz Klienta --" value="" />
                                {clients.map((c: any) => (
                                    <Picker.Item
                                        key={c.id || c.userId}
                                        label={`${c.firstName} ${c.lastName} (${c.login})`}
                                        value={c.id || c.userId}
                                    />
                                ))}
                            </Picker>
                        )}
                    />
                </View>
            </View>

            <View style={styles.group}>
                <Text style={styles.label}>Wybierz boisko sportowe</Text>
                <View style={styles.pickerContainer}>
                    <Controller
                        control={control}
                        name="roomId"
                        rules={{ required: true }}
                        render={({ field: { onChange, value } }) => (
                            <Picker
                                selectedValue={value}
                                onValueChange={onChange}
                                style={styles.picker}
                            >
                                <Picker.Item label="-- Wybierz Boisko --" value="" />
                                {rooms.map((r: any) => (
                                    <Picker.Item
                                        key={r.id}
                                        label={`${r.roomType} (${r.basePrice} PLN)`}
                                        value={r.id}
                                    />
                                ))}
                            </Picker>
                        )}
                    />
                </View>
            </View>

            <View style={styles.group}>
                <Text style={styles.label}>Start rezerwacji od</Text>

                <TouchableOpacity
                    style={styles.dateButton}
                    onPress={() => setShowDatePicker(true)}
                >
                    <Text style={styles.dateButtonText}>
                        {selectedDate
                            ? new Date(selectedDate).toLocaleString()
                            : "Kliknij, aby wybrać datę"
                        }
                    </Text>
                </TouchableOpacity>

                {showDatePicker && (
                    <DateTimePicker
                        value={selectedDate || new Date()}
                        mode="date"
                        display="default"
                        onChange={(e, date) => {
                            onDateChange(e, date);
                        }}
                    />
                )}

            </View>

            <TouchableOpacity style={styles.submitBtn} onPress={handleSubmit(onFormSubmit)}>
                <Text style={styles.submitBtnText}>Zarezerwuj</Text>
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
        marginBottom: 25,
        textAlign: 'center',
        color: '#333',
    },
    group: {
        marginBottom: 20,
    },
    label: {
        fontSize: 16,
        marginBottom: 8,
        fontWeight: '500',
        color: '#555',
    },
    pickerContainer: {
        backgroundColor: 'white',
        borderWidth: 1,
        borderColor: '#ddd',
        borderRadius: 8,
        overflow: 'hidden',
    },
    picker: {
        height: 55,
        width: '100%',
        color: '#333',
        backgroundColor: 'white'
    },
    dateButton: {
        backgroundColor: 'white',
        padding: 15,
        borderRadius: 8,
        borderWidth: 1,
        borderColor: '#ddd',
        alignItems: 'center',
    },
    dateButtonText: {
        fontSize: 16,
        color: '#333',
    },
    submitBtn: {
        marginTop: 30,
        backgroundColor: '#007AFF',
        paddingVertical: 15,
        borderRadius: 8,
        alignItems: 'center',
    },
    submitBtnText: {
        color: 'white',
        fontSize: 18,
        fontWeight: 'bold',
    },
});