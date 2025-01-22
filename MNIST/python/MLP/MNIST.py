import torch
import torch.nn as nn
import torch.optim as optim
import torch.nn.functional as F
import torchvision
import torchvision.transforms as transforms
from torch.utils.data import DataLoader, Dataset
from typing import Tuple

# -------------------------------------------------
# 1. Device Selection
# -------------------------------------------------
def get_device() -> torch.device:
    """
    Selects and returns the most suitable device:
      - Apple Silicon (MPS) if available,
      - otherwise CUDA if available,
      - otherwise CPU.
    """
    if torch.backends.mps.is_available():
        print("Using Apple Silicon MPS backend.")
        return torch.device("mps")
    elif torch.cuda.is_available():
        print("Using CUDA.")
        return torch.device("cuda")
    else:
        print("Using CPU.")
        return torch.device("cpu")

# -------------------------------------------------
# 2. Data Loading
# -------------------------------------------------
def get_mnist_loaders(batch_size: int = 64, download: bool = True) -> Tuple[DataLoader, DataLoader]:
    """
    Returns train_loader and test_loader for MNIST.

    :param batch_size: batch size for data loaders
    :param download: whether to download the MNIST dataset
    :return: (train_loader, test_loader)
    """
    transform = transforms.Compose([
        transforms.ToTensor(),
        transforms.Normalize((0.1307,), (0.3081,))
    ])
    
    # MNIST Datasets
    train_dataset: Dataset = torchvision.datasets.MNIST(
        root='./data',
        train=True,
        transform=transform,
        download=download
    )
    
    test_dataset: Dataset = torchvision.datasets.MNIST(
        root='./data',
        train=False,
        transform=transform,
        download=download
    )
    
    # Data Loaders
    train_loader: DataLoader = DataLoader(train_dataset, batch_size=batch_size, shuffle=True)
    test_loader: DataLoader = DataLoader(test_dataset, batch_size=batch_size, shuffle=False)
    
    return train_loader, test_loader

# -------------------------------------------------
# 3. Model Definitions
# -------------------------------------------------
class NaiveMLP(nn.Module):
    def __init__(self) -> None:
        super(NaiveMLP, self).__init__()
        # A simple 3-layer MLP
        self.fc1 = nn.Linear(28 * 28, 256)
        self.fc2 = nn.Linear(256, 128)
        self.fc3 = nn.Linear(128, 10)
        
    def forward(self, x: torch.Tensor) -> torch.Tensor:
        """
        Forward pass for the MLP.
        Expects input shape: (B, 1, 28, 28).
        """
        x = x.view(x.size(0), -1)   # flatten to (B, 784)
        x = F.relu(self.fc1(x))
        x = F.relu(self.fc2(x))
        x = self.fc3(x)
        return x

class SimpleCNN(nn.Module):
    def __init__(self) -> None:
        super(SimpleCNN, self).__init__()
        # Convolutional layers
        self.conv1 = nn.Conv2d(in_channels=1, out_channels=32, kernel_size=3, padding=1)
        self.conv2 = nn.Conv2d(in_channels=32, out_channels=64, kernel_size=3, padding=1)
        
        # Max-pooling
        self.pool = nn.MaxPool2d(kernel_size=2, stride=2)
        
        # Fully connected layers
        # After two 2x2 poolings on 28x28 -> (28 -> 14 -> 7)
        self.fc1 = nn.Linear(64 * 7 * 7, 128)
        self.fc2 = nn.Linear(128, 10)
        
    def forward(self, x: torch.Tensor) -> torch.Tensor:
        """
        Forward pass for the CNN.
        Expects input shape: (B, 1, 28, 28).
        """
        x = F.relu(self.conv1(x))   # (B, 32, 28, 28)
        x = self.pool(x)            # (B, 32, 14, 14)
        x = F.relu(self.conv2(x))   # (B, 64, 14, 14)
        x = self.pool(x)            # (B, 64, 7, 7)
        
        x = x.view(x.size(0), -1)   # flatten to (B, 64*7*7)
        x = F.relu(self.fc1(x))
        x = self.fc2(x)
        return x

# -------------------------------------------------
# 4. Training and Testing
# -------------------------------------------------
def train(
    model: nn.Module,
    device: torch.device,
    train_loader: DataLoader,
    optimizer: optim.Optimizer,
    criterion: nn.Module,
    epoch: int
) -> None:
    """
    Train the model for one epoch.

    :param model: PyTorch model
    :param device: device to run on (CPU, CUDA, or MPS)
    :param train_loader: DataLoader for training data
    :param optimizer: optimizer (e.g. Adam)
    :param criterion: loss function (e.g. CrossEntropyLoss)
    :param epoch: current epoch (for logging)
    """
    model.train()
    running_loss = 0.0
    
    for batch_idx, (inputs, targets) in enumerate(train_loader):
        inputs, targets = inputs.to(device), targets.to(device)
        
        optimizer.zero_grad()
        outputs = model(inputs)
        loss = criterion(outputs, targets)
        loss.backward()
        optimizer.step()
        
        running_loss += loss.item()
        if (batch_idx + 1) % 100 == 0:
            print(
                f"Epoch [{epoch}], Step [{batch_idx+1}/{len(train_loader)}], "
                f"Loss: {running_loss / 100:.4f}"
            )
            running_loss = 0.0

def test(
    model: nn.Module,
    device: torch.device,
    test_loader: DataLoader
) -> float:
    """
    Evaluate the model on the test set.

    :param model: PyTorch model
    :param device: device to run on
    :param test_loader: DataLoader for testing data
    :return: accuracy in percentage
    """
    model.eval()
    correct = 0
    total = 0
    
    with torch.no_grad():
        for inputs, targets in test_loader:
            inputs, targets = inputs.to(device), targets.to(device)
            outputs = model(inputs)
            _, predicted = torch.max(outputs, 1)
            total += targets.size(0)
            correct += (predicted == targets).sum().item()
    
    accuracy = 100.0 * correct / total
    return accuracy

# -------------------------------------------------
# 5. Main Entry Point
# -------------------------------------------------
if __name__ == "__main__":
    # Hyperparameters
    BATCH_SIZE = 64
    EPOCHS = 5
    LEARNING_RATE = 1e-3
    
    # Select device (MPS, CUDA, or CPU)
    device: torch.device = get_device()
    
    # Get Data Loaders
    train_loader, test_loader = get_mnist_loaders(batch_size=BATCH_SIZE, download=True)
    
    # Option 1: MLP Model
    print("\n=== Training Naive MLP ===")
    mlp_model: nn.Module = NaiveMLP().to(device)
    criterion: nn.Module = nn.CrossEntropyLoss()
    optimizer: optim.Optimizer = optim.Adam(mlp_model.parameters(), lr=LEARNING_RATE)
    
    for epoch in range(1, EPOCHS + 1):
        train(
            model=mlp_model,
            device=device,
            train_loader=train_loader,
            optimizer=optimizer,
            criterion=criterion,
            epoch=epoch
        )
        mlp_accuracy = test(
            model=mlp_model,
            device=device,
            test_loader=test_loader
        )
        print(f"MLP Test Accuracy after epoch {epoch}: {mlp_accuracy:.2f}%")
    
    # Option 2: CNN Model
    print("\n=== Training Simple CNN ===")
    cnn_model: nn.Module = SimpleCNN().to(device)
    optimizer = optim.Adam(cnn_model.parameters(), lr=LEARNING_RATE)
    
    for epoch in range(1, EPOCHS + 1):
        train(
            model=cnn_model,
            device=device,
            train_loader=train_loader,
            optimizer=optimizer,
            criterion=criterion,
            epoch=epoch
        )
        cnn_accuracy = test(
            model=cnn_model,
            device=device,
            test_loader=test_loader
        )
        print(f"CNN Test Accuracy after epoch {epoch}: {cnn_accuracy:.2f}%")
