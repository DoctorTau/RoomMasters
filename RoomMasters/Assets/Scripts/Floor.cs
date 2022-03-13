using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using FurnitureStruct;

public class Floor : MonoBehaviour
{
    public Vector2Int GridSize = new Vector2Int(10, 10);
    public List<GameObject> buttons;
    private GameObject[,] grid;
    private GameObject selectedObject;
    private Camera mainCamera;
    private float deltaTime = 0.3f;
    bool dragging = false;
    private StartPosition startPosition;

    private void Awake()
    {
        grid = new GameObject[GridSize.x, GridSize.y];
        mainCamera = Camera.main;
        foreach (var button in buttons) button.SetActive(false);
    }

    /// <summary>
    /// Adds new object to the room. 
    /// </summary>
    /// <param name="furniturePrehub">Prefab of addeding object.</param>
    public void CreateNewObject(GameObject furniturePrehub)
    {
        if (selectedObject != null)
            CancelSelection();

        SelectObject(Instantiate(furniturePrehub));
    }

    /// <summary>
    /// Sets object to the grid. 
    /// </summary>
    /// <param name="placeX">X coordinate of object.</param>
    /// <param name="placeY">Y coordinate of object.</param>
    private void SetObjectToGrid(int placeX, int placeY)
    {
        if (selectedObject == null) return;

        Debug.Log("Here");

        Furniture furniture = selectedObject.GetComponent<Furniture>();
        for (int x = 0; x < furniture.Size.x; x++)
        {
            for (int y = 0; y < furniture.Size.y; y++)
            {
                grid[placeX + x, placeY + y] = selectedObject;
                Debug.Log("Done" + grid[placeX + x, placeY + y]);
            }
        }
    }

    /// <summary>
    /// Deletes an object from the grid. 
    /// </summary>
    /// <param name="obj">Object to delete</param>
    private void DeleteObjectFromGrid(GameObject obj)
    {
        for (int i = 0; i < grid.GetLength(0); i++)
        {
            for (int j = 0; j < grid.GetLength(1); j++)
                if (grid[i, j] == obj) grid[i, j] = null;
        }
    }

    /// <summary>
    /// Checks if object position has another object. 
    /// </summary>
    /// <param name="obj">Object to check.</param>
    /// <param name="placeX">X coordinate of position.</param>
    /// <param name="placeY">Y coordinate of position.</param>
    /// <returns>True if position clears, otherwise false.</returns>
    private bool CheckCellsForObject(GameObject obj, int placeX, int placeY)
    {
        Furniture furniture = selectedObject.GetComponent<Furniture>();
        for (int x = 0; x < furniture.Size.x; x++)
        {
            for (int y = 0; y < furniture.Size.y; y++)
            {
                if (grid[placeX + x, placeY + y] != null) return false;
            }
        }
        return true;
    }

    /// <summary>
    /// Sets selected object back to its position and deselect it. 
    /// </summary>
    public void CancelMovement()
    {
        if (selectedObject == null) return;

        selectedObject.transform.position = startPosition.position;
        selectedObject.transform.rotation = startPosition.rotation;
        CancelSelection();
    }

    /// <summary>
    /// Deselects object and puts it to the position, if it's posible.
    /// </summary>
    public void CancelSelection()
    {
        var selectedObjectPosition = selectedObject.transform.position; // Position of selected object.
        if (!CheckCellsForObject(selectedObject, (int)selectedObjectPosition.x, (int)selectedObjectPosition.z)) return; // Check if cells are free.
        selectedObject.GetComponent<Outline>().OutlineMode = Outline.Mode.OutlineHidden;
        DeleteObjectFromGrid(selectedObject); // Delete object from grid.
        SetObjectToGrid((int)selectedObjectPosition.x, (int)selectedObjectPosition.z); // Set object to grid on new position.
        selectedObject = null; // Clear selection.
        foreach (var button in buttons) button.SetActive(false); // Hide the buttons.
    }

    /// <summary>
    /// Selects an object.
    /// </summary>
    /// <param name="obj">Object which is selected.</param>
    private void SelectObject(GameObject obj)
    {
        foreach (var button in buttons) button.SetActive(true); // Show context buttons.
        selectedObject = obj; // Set selected object to variable.
        startPosition = new StartPosition(selectedObject.transform.position, selectedObject.transform.rotation); // Saves the position of the selected object.
        var selection = selectedObject.GetComponent<Outline>(); // Gets selection of selected object.
        DeleteObjectFromGrid(selectedObject); // Delete selected object from grid.
        selection.OutlineMode = Outline.Mode.OutlineVisible; // Shows selection color.
    }

    /// <summary>
    ///  Rotates selected object to 90 degrees. 
    /// </summary>
    public void RotateSelectedObject()
    {
        var rotate = Quaternion.Euler(0, (selectedObject.transform.rotation.eulerAngles.y + 90) % 360, 0);
        selectedObject.transform.rotation = rotate;
    }

    /// <summary>
    /// Sets color of the selection by object availability.
    /// </summary>
    /// <param name="available">Is it possible to set an object.</param>
    private void SetTransparentForObject(bool available)
    {
        if (selectedObject == null) return;
        var selection = selectedObject.GetComponent<Outline>();
        if (!available)
            selection.OutlineColor = Color.red;
        else
            selection.OutlineColor = Color.yellow;
    }

    void Update()
    {
        // If user have selected an object. 
        if (selectedObject != null)
        {
            // Checks object availability.
            var selectedObjectPosition = selectedObject.transform.position;
            SetTransparentForObject(CheckCellsForObject(selectedObject,
                                                        (int)selectedObjectPosition.x,
                                                        (int)selectedObjectPosition.z));

            // Get size of an object.
            var selectedObjectSizeX = selectedObject.GetComponent<Furniture>().Size.x;
            var selectedObjectSizeY = selectedObject.GetComponent<Furniture>().Size.y;
            // Get touch on screen if we have one.
            if (Input.touchCount > 0 && (Input.GetTouch(0).phase == TouchPhase.Began
                || Input.GetTouch(0).phase == TouchPhase.Moved))
            {
                Touch touch = Input.touches[0];
                Vector3 positionOfTouch = touch.position;

                // Create plane, to raycast.
                var groundPlane = new Plane(Vector3.up, Vector3.zero);

                // Ray cast to calculate new position if touch.
                Ray ray = mainCamera.ScreenPointToRay(positionOfTouch);
                if (groundPlane.Raycast(ray, out float position))
                {
                    // Get coordinates.
                    Vector3 worldPosition =
                        ((Ray)mainCamera.ScreenPointToRay(positionOfTouch)).GetPoint(position);
                    // Round coordinates, to place object to the grid.
                    worldPosition.x = Mathf.RoundToInt(worldPosition.x);
                    worldPosition.z = Mathf.RoundToInt(worldPosition.z);
                    // Check if on floor boarders.
                    if (worldPosition.x - selectedObjectSizeX < GridSize.x - 1
                        && worldPosition.z - selectedObjectSizeY < GridSize.y - 1
                        && worldPosition.x >= 0 && worldPosition.z >= 0)
                        // Set new object position.
                        selectedObject.transform.position = worldPosition;
                }
            }
        }

        else
        {
            // Check if user has touched screen. 
            if (Input.touchCount > 0 && Input.GetTouch(0).phase == TouchPhase.Stationary)
            {
                // Get Touch.
                Touch touch = Input.GetTouch(0);
                Ray ray = mainCamera.ScreenPointToRay(touch.position);
                // Raycast to deffine the object to select.
                if (Physics.Raycast(ray, out RaycastHit hit))
                {
                    if (hit.collider.gameObject.tag == "FloarFurniture")
                    {
                        // Counts time that user pressed to the object.
                        if (deltaTime > 0) deltaTime -= Time.deltaTime;
                        else
                        {
                            // Select chosen object.
                            SelectObject(hit.collider.gameObject);
                            deltaTime = 0.3f;
                        }
                    }
                }
            }
        }
    }
}

