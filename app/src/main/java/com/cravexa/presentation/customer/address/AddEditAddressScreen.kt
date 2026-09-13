package com.cravexa.presentation.customer.address

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.cravexa.core.designsystem.components.CravexaButton
import com.cravexa.core.designsystem.components.CravexaButtonStyle
import com.cravexa.core.designsystem.components.CravexaChip
import com.cravexa.core.designsystem.components.CravexaTextField
import com.cravexa.core.designsystem.components.CravexaTopAppBar
import com.cravexa.core.designsystem.theme.CravexaPurple800
import com.cravexa.domain.model.AddressType

@Composable
fun AddEditAddressScreen(
    onBack: () -> Unit,
    viewModel: AddressViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val editingId by viewModel.editingAddressId.collectAsState()

    val fullName by viewModel.fullName.collectAsState()
    val phone by viewModel.phone.collectAsState()
    val houseBuilding by viewModel.houseBuilding.collectAsState()
    val street by viewModel.street.collectAsState()
    val area by viewModel.area.collectAsState()
    val city by viewModel.city.collectAsState()
    val state by viewModel.state.collectAsState()
    val pincode by viewModel.pincode.collectAsState()
    val landmark by viewModel.landmark.collectAsState()
    val addressType by viewModel.addressType.collectAsState()
    val isDefault by viewModel.isDefault.collectAsState()

    val nameError by viewModel.nameError.collectAsState()
    val phoneError by viewModel.phoneError.collectAsState()
    val houseError by viewModel.houseError.collectAsState()
    val streetError by viewModel.streetError.collectAsState()
    val areaError by viewModel.areaError.collectAsState()
    val pincodeError by viewModel.pincodeError.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val focusManager = LocalFocusManager.current
    val isSaving = uiState is AddressUiState.Saving

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is AddressUiState.Success -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
                onBack()
            }
            is AddressUiState.Error -> {
                snackbarHostState.showSnackbar(state.message)
                viewModel.resetState()
            }
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            CravexaTopAppBar(
                title = if (editingId == null) "Add Delivery Address" else "Edit Address",
                showBackButton = true,
                onBackClick = onBack
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            // Address Type Selector
            Text(
                text = "Address Type",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AddressType.entries.forEach { type ->
                    CravexaChip(
                        text = type.name,
                        selected = addressType == type,
                        onClick = { viewModel.onAddressTypeChanged(type) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contact Info
            CravexaTextField(
                value = fullName,
                onValueChange = viewModel::onFullNameChanged,
                label = "Full Name",
                placeholder = "e.g. Ananya Sharma",
                errorMessage = nameError,
                enabled = !isSaving,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(12.dp))

            CravexaTextField(
                value = phone,
                onValueChange = viewModel::onPhoneChanged,
                label = "Mobile Number (10 digits)",
                placeholder = "9876543210",
                errorMessage = phoneError,
                enabled = !isSaving,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Location Info
            CravexaTextField(
                value = houseBuilding,
                onValueChange = viewModel::onHouseBuildingChanged,
                label = "Flat / House No. / Building Name",
                placeholder = "e.g. Flat 402, Shanti Nilayam",
                errorMessage = houseError,
                enabled = !isSaving,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(12.dp))

            CravexaTextField(
                value = street,
                onValueChange = viewModel::onStreetChanged,
                label = "Street / Road Name",
                placeholder = "e.g. 12th Main Road, HAL 2nd Stage",
                errorMessage = streetError,
                enabled = !isSaving,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(12.dp))

            CravexaTextField(
                value = area,
                onValueChange = viewModel::onAreaChanged,
                label = "Area / Locality",
                placeholder = "e.g. Indiranagar",
                errorMessage = areaError,
                enabled = !isSaving,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(12.dp))

            CravexaTextField(
                value = landmark,
                onValueChange = viewModel::onLandmarkChanged,
                label = "Landmark (Optional)",
                placeholder = "e.g. Near Metro Pillar 84",
                enabled = !isSaving,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                CravexaTextField(
                    value = city,
                    onValueChange = viewModel::onCityChanged,
                    label = "City",
                    enabled = !isSaving,
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )

                CravexaTextField(
                    value = pincode,
                    onValueChange = viewModel::onPincodeChanged,
                    label = "PIN Code",
                    placeholder = "560038",
                    errorMessage = pincodeError,
                    enabled = !isSaving,
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus()
                        viewModel.saveAddress()
                    })
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Default Checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isDefault,
                    onCheckedChange = viewModel::onIsDefaultChanged,
                    colors = CheckboxDefaults.colors(checkedColor = CravexaPurple800)
                )
                Text(
                    text = "Make this my default delivery address",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Submit Button
            CravexaButton(
                text = if (editingId == null) "Save Address" else "Update Address",
                onClick = {
                    focusManager.clearFocus()
                    viewModel.saveAddress()
                },
                isLoading = isSaving,
                enabled = !isSaving,
                style = CravexaButtonStyle.PRIMARY
            )

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

